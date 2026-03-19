#!/usr/bin/env bash
# Pre-download the ONNX model so first tool call doesn't block on network I/O.
# Requires: node, npm packages installed
set -euo pipefail

MODEL="${1:-nomic-ai/nomic-embed-text-v1.5}"

node -e "
const { pipeline } = require('@huggingface/transformers');
console.log('Downloading model:', '$MODEL');
pipeline('feature-extraction', '$MODEL', { dtype: 'fp32' })
  .then(() => console.log('Model cached successfully.'))
  .catch(err => { console.error(err); process.exit(1); });
"
