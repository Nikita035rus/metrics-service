INSERT INTO metrics (metric_timestamp, metric_value, payload, client_id)
VALUES
    ('2024-01-15 10:30:00+00', 15.5, '{"source": "app", "type": "response_time"}', 'client-1'),
    ('2024-01-15 10:30:01+00', 22.1, '{"source": "api", "type": "latency"}', 'client-1'),
    ('2024-01-15 10:30:02+00', 8.7, '{"source": "db", "type": "query_time"}', 'client-2'),
    ('2024-01-15 10:30:03+00', 45.2, '{"source": "cache", "type": "hit_rate"}', 'client-1'),
    ('2024-01-15 10:30:04+00', 12.3, '{"source": "network", "type": "bandwidth"}', 'client-3'),
    ('2024-01-15 10:30:05+00', 33.8, '{"source": "app", "type": "memory_usage"}', 'client-2');
