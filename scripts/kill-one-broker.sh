#!/usr/bin/env bash
set -euo pipefail

ACTION=${1:-stop}   # stop|start|restart
BROKER=${2:-kafka-2}

echo "[*] $ACTION $BROKER"
case "$ACTION" in
  stop) docker stop "$BROKER" ;;
  start) docker start "$BROKER" ;;
  restart) docker restart "$BROKER" ;;
  *) echo "usage: $0 stop|start|restart [kafka-1|kafka-2|kafka-3]"; exit 1 ;;
esac