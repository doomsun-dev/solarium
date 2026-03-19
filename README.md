# Heliograph

Shared knowledge base MCP server for the Doomsun team. Backed by [Qdrant](https://qdrant.tech/) vector database with local embeddings ([nomic-embed-text-v1.5](https://huggingface.co/nomic-ai/nomic-embed-text-v1.5) via HuggingFace Transformers + ONNX Runtime).

Heliograph runs as a local MCP server process — embeddings are computed on your machine, vectors are stored in our shared Qdrant Cloud instance.

## Setup

### Prerequisites

- Node.js 20+
- npm

### 1. Install dependencies

```bash
cd heliograph
npm install
```

### 2. Build and link

```bash
npm run build
npm link
```

This builds the server and creates a global `heliograph` command on your PATH.

### 3. Pre-download the embedding model (optional but recommended)

The model (~130MB) is downloaded automatically on first use, but you can cache it ahead of time:

```bash
./bin/download-model.sh
```

### 4. Get the Qdrant Cloud API key

Ask Joe for the `QDRANT_API_KEY`. Add it to your shell profile (`~/.zshrc` or `~/.bashrc`):

```bash
# Qdrant Cloud (Heliograph)
export QDRANT_API_KEY=<api-key-from-joe>
```

Then reload your shell: `source ~/.zshrc`

### 5. Add to your Claude Code MCP config

Add the following to `.mcp.json` in any repo where you want Heliograph available (it's already configured in `nova`):

```json
{
  "mcpServers": {
    "heliograph": {
      "command": "heliograph",
      "env": {
        "QDRANT_URL": "https://0c51095f-01c7-4b52-9a2f-74a5df98d877.us-east-1-1.aws.cloud.qdrant.io:6333"
      }
    }
  }
}
```

`QDRANT_API_KEY` is read from your shell environment automatically.

### 6. Verify

In Claude Code, run `/mcp` to confirm Heliograph is connected, then try:

> Search the knowledge base for "portfolio engine"

## Available Tools

| Tool | Description |
|------|-------------|
| `store_document` | Store a document (chunks and embeds for semantic search) |
| `search` | Semantic search across all documents |
| `list_documents` | List all documents with metadata |
| `read_document` | Read full document content by ID |
| `update_document` | Update content or metadata (re-embeds if content changes) |
| `delete_document` | Delete a document and all its chunks |
| `list_tags` | List all tags with counts |
| `tag_document` | Add tags to a document |
| `untag_document` | Remove tags from a document |

## Configuration

All configuration is via environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `QDRANT_URL` | `http://localhost:6333` | Qdrant server URL |
| `QDRANT_API_KEY` | _(none)_ | Qdrant API key (required for cloud) |
| `COLLECTION_NAME` | `knowledge` | Qdrant collection name |
| `MODEL_NAME` | `nomic-ai/nomic-embed-text-v1.5` | HuggingFace embedding model |
| `CHUNK_MAX_CHARS` | `1600` | Max characters per chunk |
| `CHUNK_OVERLAP` | `200` | Overlap between chunks |

## Development

```bash
# Watch mode (auto-rebuild on changes)
npm run watch

# Run tests
npm test
```

Built with shadow-cljs (ClojureScript).
