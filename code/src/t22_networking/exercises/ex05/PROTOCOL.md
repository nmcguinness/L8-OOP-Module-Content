# Leaderboard Server — Protocol Reference

Transport: TCP socket | Format: one JSON object per line (UTF-8) | Port: 9103


## Request envelope
{ "requestType": "<TYPE>", "payload": { ... } }

## Response envelope
{ "status": "OK|ERROR", "message": "<description>", "data": <payload or null> }

## Request types

| requestType | Payload fields | Success data |
|---------------|--------------------------------------------------------|------------------------|
| GET_ALL | (none) | Array of ScoreEntry |
| SUBMIT_SCORE | playerId, playerName, score (int), gameMode (string) | Inserted ScoreEntry |
| GET_TOP_N | n (int) | Array of ScoreEntry |
| DISCONNECT | (none) | null |

## Error responses
Any request that fails returns: { "status": "ERROR", "message": "<reason>", "data": null } Always check status before using data.
