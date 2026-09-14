#!/bin/bash
# Sanity-checks GIFT question files. Reports structural errors that would
# make a Moodle import fail or silently produce a malformed question.
status=0
for f in "$@"; do
  q=$(grep -c '^::' "$f")
  open=$(grep -c '^{$' "$f")
  close=$(grep -c '^}$' "$f")
  if [ "$q" -ne "$open" ] || [ "$q" -ne "$close" ]; then
    echo "FAIL $f: $q questions but $open '{' and $close '}' lines"; status=1
  fi
  # exactly one correct answer (= marker) per question block
  bad=$(awk '
    /^\{$/ { inblk=1; correct=0; opts=0; next }
    /^\}$/ { if (inblk) { if (correct != 1) printf "%d ", NR; if (opts < 3) printf "opts@%d ", NR } inblk=0; next }
    inblk && /^  = / { correct++; opts++ }
    inblk && /^  ~ / { opts++ }
  ' "$f")
  if [ -n "$bad" ]; then
    echo "FAIL $f: block(s) ending at line(s) $bad lack exactly one '=' answer or have <4 options"; status=1
  fi
  # unescaped GIFT control characters in question/answer text
  if grep -nE '(^|[^\])[{}]' "$f" | grep -vE '^[0-9]+:[{}]$' | grep -q .; then
    echo "WARN $f: possible unescaped { or } in text:"
    grep -nE '(^|[^\])[{}]' "$f" | grep -vE '^[0-9]+:[{}]$' | head -3
  fi
  # every question needs feedback on every option
  o=$(grep -cE '^  [=~] ' "$f"); fb=$(grep -c '^    #' "$f")
  if [ "$o" -ne "$fb" ]; then
    echo "FAIL $f: $o options but $fb feedback lines"; status=1
  fi
done
[ $status -eq 0 ] && echo "OK: all files structurally valid"
exit $status
