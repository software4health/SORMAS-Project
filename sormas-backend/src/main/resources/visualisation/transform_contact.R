library(RPostgreSQL)
library(visNetwork)
library(dplyr)

envDefaults = c(
  "DB_USER" = "sormas_user", "DB_PASS" = "sormas_db",
  "DB_HOST" = "127.0.0.1", "DB_PORT" = "5432", "DB_NAME" = "sormas_db",
  "CONTACT_IDS" = "",
  "OUTFILE" = "sormas_contact.html",
  "HIERARCHICAL" = "FALSE"
)
sysEnv = Sys.getenv(names(envDefaults), NA);
sysEnv = subset(sysEnv, sysEnv == sysEnv)

env = c(sysEnv, envDefaults)[names(envDefaults)]

DB_USER = env["DB_USER"]
DB_PASS = env["DB_PASS"]
DB_HOST = env["DB_HOST"]
DB_NAME = env["DB_NAME"]
DB_PORT = env["DB_PORT"]

OUTFILE = env["OUTFILE"]
CONTACT_IDS = env["CONTACT_IDS"]
HIERARCHICAL = env["HIERARCHICAL"]

caseClass = paste0("{", "Classification.", c(
  "HEALTHY",
  "NOT_CLASSIFIED",
  "SUSPECT",
  "PROBABLE",
  "CONFIRMED",
  "CONFIRMED_NO_SYMPTOMS",
  "CONFIRMED_UNKNOWN_SYMPTOMS"), "}")

# see CountElementStyle.POSITIVE and styles.css count-element
caseClassColor = c(
  "#32CD32", # positive (==healthy)
  "#808080", # minor
  "#c8aa00", # relevant
  "#be6900", # important
  "#c80000", # critical
  "#C80000B2", # critical transparent
  "#C800007F") # critical more transparent

hierarchical = "T" == substr(HIERARCHICAL, 1, 1)
defaultFont = "font-family:'Open Sans', sans-serif, 'Source Sans Pro'"
mainStyle = paste(defaultFont, "color: #6591C4", ";font-weight: 600", "font-size: 1.6em", "text-align:center;", sep = "; ")
submainStyle = paste(defaultFont, "text-align:center;", sep = "; ")
footerStyle = defaultFont

#connection to db
con = dbConnect(PostgreSQL(), user = DB_USER, dbname = DB_NAME, password = DB_PASS, host = DB_HOST, port = DB_PORT)  # connect to load db

#query contact table and ratin only contacts parsed from Sys.getenv
if (CONTACT_IDS == "") {
  #for testing: get all valid contacts
  idContString = paste(dbGetQuery(con, "select ct.id
from public.contact ct
	join public.cases cs on (ct.caze_id = cs.id)
where ct.deleted = FALSE and ct.contactclassification != 'NO_CONTACT'
	and cs.caseclassification != 'NO_CASE' and cs.deleted = FALSE")$id, collapse=",")
} else {
  idContString = CONTACT_IDS
}
#guards against syntax exception due to empty id list
if (idContString == "") idContString = "NULL"

#query all relevant contacts
sql_edge = "select distinct cs.person_id case_pid, ct.person_id contact_pid,
	'ContactProximity.' || ct.contactproximity as contactproximity, 'ContactStatus.' || ct.contactstatus as contactstatus
from public.contact ct
	join public.cases cs on ct.caze_id = cs.id
where ct.id in (%s)"

edgeTable = dbGetQuery(con, sprintf(sql_edge, idContString))

#query all relevant persons with oldest relevant case
sql_node = "with clean_ct as (
	select *
	from public.contact
	where id in (%s)
),
clean_cs as (
	select *
	from public.cases
	where deleted = FALSE
		and caseclassification != 'NO_CASE'
),
node as (
	--contact / resulting case
	select p.id as person_id, rcs.reportdate, rcs.uuid, rcs.caseclassification
	from public.person p
		join clean_ct ct on ct.person_id = p.id
		left join clean_cs rcs on ct.resultingcase_id = rcs.id
union
	--caze
	select distinct p.id, cs.reportdate, cs.uuid, cs.caseclassification
	from clean_ct ct
		join public.cases cs on ct.caze_id = cs.id
		join public.person p on cs.person_id = p.id
)
-- take data from earliest case
select distinct on (person_id)
	person_id, uuid,  upper(left(uuid, 6)) as short_uuid, 'Classification.' || caseclassification as caseclassification
from node
order by person_id, uuid is null, reportdate, uuid"

nodeTable = dbGetQuery(con, sprintf(sql_node, idContString))

dbDisconnect(con)

elist = data.frame(from = edgeTable$case_pid, to = edgeTable$contact_pid, contactproximity = edgeTable$contactproximity)


nlist = data.frame(id = nodeTable$person_id, uuid = nodeTable$uuid, label = nodeTable$short_uuid, Classification = factor(paste0("{", nodeTable$caseclassification, "}"), levels = caseClass), stringsAsFactors = FALSE)
# "healthy"
nlist$Classification[is.na(nlist$Classification)] <- caseClass[1]

## defining contact categories based on proximity
highRiskProximity = paste("ContactProximity", c(
  "FACE_TO_FACE_LONG",
  "TOUCHED_FLUID",
  "MEDICAL_UNSAFE",
  "CLOTHES_OR_OTHER",
  "PHYSICAL_CONTACT"), sep = ("."))
elist$label = NA
elist$label[elist$contactproximity %in% highRiskProximity] = "1"
elist$label[!(elist$contactproximity %in% highRiskProximity)] = "2"
#drop contactproximity
elist$contactproximity <- NULL

## defining plotting parameters  

nodesS = nlist
edgesS = elist

#deleting duplicate edges
edgesS <- edgesS[order(edgesS$label),]
edgesS <- distinct(edgesS, from, to, .keep_all = TRUE)

# deleting edges linking a node to itselt
edgesS = edgesS[edgesS$from != edgesS$to,]

# defining edge attributes
edgesS$dashes = TRUE
edgesS$dashes[edgesS$label == 1] = FALSE #  using broken lines for high risk contacts

# defining node attributes
nodesS$group = nodesS$Classification
#nodesS$shape = c("icon")
#nodesS$code = c("f007")
#nodesS$shadow = F, 

# defining legend
addNodesS <- data.frame(
  label = c(caseClass, "1 = {highRisk}", "2 = {lowRisk}"),
  shape = "icon",
  icon.code = c(rep("f007", times = length(caseClass)), "f178", "f178"),
  icon.size = c(rep(25, times = length(caseClass)), 25, 25),
  icon.color = c(caseClassColor, "#0d0c0c", "#0d0c0c"))

#plotting

avertarIcon <- function(color, code = c("f007")) {
  list(face = 'FontAwesome', code = code, color = color)
}

g = visNetwork(nodesS, edgesS, main = list(text = "{heading}", style = mainStyle),
               submain = list(text = "{subheading}", style = submainStyle),
               #footer = list(text = "Double click on the icon to open the associated case or contact data", style = footerStyle),
               background = "white", annot = T, width = "100%") %>%
  visEdges(arrows = "to", color = "black", smooth = list(type = "continuous")) %>%
  visOptions(selectedBy = list(variable = "Classification", main="{selectByClassification}", sort = FALSE, values = caseClass, style = "width: auto; height: auto;"), highlightNearest = TRUE, nodesIdSelection = FALSE)
for (i in 1:length(caseClass)) {
  g = visGroups(graph = g, groupname = caseClass[i], size = 10, shape = "icon", icon = avertarIcon(caseClassColor[i]))
}
g = g %>%
  addFontAwesome() %>%
  visLegend(addNodes = addNodesS, useGroups = F, position = "left", width = 0.2, ncol = 1, main="{legend}")

if (hierarchical) {
  g = g %>%
    visHierarchicalLayout() %>%
    visPhysics(hierarchicalRepulsion = list(damping = 0.26))
} else {
  g = g %>%
    visPhysics(solver = "barnesHut", barnesHut = list(damping = 0.26, avoidOverlap = 0.2))
}

g = g %>%
  visInteraction(dragNodes = T, dragView = T, zoomView = T)

visNetwork::visSave(g, OUTFILE, selfcontained = FALSE)