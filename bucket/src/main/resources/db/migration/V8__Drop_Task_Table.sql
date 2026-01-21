-- Migration to drop Task table
-- Removing task table as it's not needed for the SSE notification demo

DROP TABLE IF EXISTS task CASCADE;
