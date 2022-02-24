#!/bin/bash
set -e

# Set up the database
echo "Starting database setup..."

SORMAS_POSTGRES_USER=sormas_user
SORMAS_POSTGRES_PASSWORD=password
DB_NAME=sormas
DB_NAME_AUDIT=sormas_audit

psql -v ON_ERROR_STOP=1 --username "${SORMAS_POSTGRES_USER}" <<EOSQL
    CREATE DATABASE ${DB_NAME} WITH OWNER = '${SORMAS_POSTGRES_USER}' ENCODING = 'UTF8';
    CREATE DATABASE ${DB_NAME_AUDIT} WITH OWNER = '${SORMAS_POSTGRES_USER}' ENCODING = 'UTF8';
    \c ${DB_NAME}
    CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql;
    ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO ${SORMAS_POSTGRES_USER};
    CREATE EXTENSION temporal_tables;
    CREATE EXTENSION pg_trgm;
    CREATE EXTENSION pgcrypto;
    CREATE EXTENSION pg_stat_statements;
    CREATE EXTENSION IF NOT EXISTS unaccent;
    GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO ${SORMAS_POSTGRES_USER};
    \c ${DB_NAME_AUDIT}
    CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
    GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO ${SORMAS_POSTGRES_USER};
    ALTER TABLE IF EXISTS schema_version OWNER TO ${SORMAS_POSTGRES_USER};
EOSQL

psql -v ON_ERROR_STOP=1 --username "${SORMAS_POSTGRES_USER}" -d ${DB_NAME} -f /tmp/sormas_schema.sql