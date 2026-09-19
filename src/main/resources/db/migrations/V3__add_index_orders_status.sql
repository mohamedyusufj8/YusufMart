-- V3__add_index_orders_status.sql
-- Add index on orders status for performance

CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
