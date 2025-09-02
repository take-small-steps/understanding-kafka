#!/usr/bin/env bash
set -euo pipefail
TOPIC=${1:-acks-demo}
NUM=${2:-100000}
SIZE=${3:-200}

echo "[*] acks=0, records=$NUM, size=$SIZE bytes"
docker exec -it kafka-1 kafka-producer-perf-test \
  --topic "$TOPIC" \
  --num-records "$NUM" \
  --record-size "$SIZE" \
  --throughput -1 \
  --producer-props bootstrap.servers=kafka-1:9092 acks=0 linger.ms=5