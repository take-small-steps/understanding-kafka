#!/usr/bin/env bash
set -euo pipefail

TOPIC=${1:-acks-demo}
PARTITIONS=${2:-3}
RF=3

echo "[+] create topic: $TOPIC (partitions=$PARTITIONS, RF=$RF, min.insync.replicas=2)"
docker exec -it kafka-1 kafka-topics \
  --create \
  --topic "$TOPIC" \
  --partitions "$PARTITIONS" \
  --replication-factor "$RF" \
  --bootstrap-server kafka-1:9092 \
  --config min.insync.replicas=2

echo "[+] describe"
docker exec -it kafka-1 kafka-topics \
  --describe --topic "$TOPIC" --bootstrap-server kafka-1:9092