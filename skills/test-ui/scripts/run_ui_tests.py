#!/usr/bin/env python3
"""Run the Markdown-defined Janet UI tests and stop at the first failure."""

from pathlib import Path
import re
import subprocess
import sys
import tempfile

ROOT = Path(__file__).resolve().parents[3]
PLAN = ROOT / "test" / "ui-test-plan.md"


def read_cases(text):
    cases = []
    sections = re.split(r"(?=^## Test case: )", text, flags=re.MULTILINE)
    for section in sections:
        heading = re.search(r"^## Test case: (.+)$", section, re.MULTILINE)
        if not heading:
            continue
        aim = re.search(r"^Aim:\s*(.+)$", section, re.MULTILINE)
        blocks = re.findall(r"```(?:text|console)?\n(.*?)```", section, re.DOTALL)
        if not aim or len(blocks) < 2:
            raise ValueError(f"Malformed test case: {heading.group(1)}")
        cases.append((heading.group(1), aim.group(1), blocks[0], blocks[1]))
    return cases


def run():
    if not PLAN.exists():
        raise SystemExit(f"Missing test plan: {PLAN}")
    cases = read_cases(PLAN.read_text())
    if not cases:
        raise SystemExit("No test cases found in the UI test plan.")

    with tempfile.TemporaryDirectory(prefix="janet-ui-") as build_dir:
        sources = [str(path) for path in (ROOT / "src" / "main" / "java").rglob("*.java")]
        subprocess.run(["javac", "--release", "25", "-d", build_dir, *sources], cwd=ROOT, check=True)
        for index, (name, aim, inputs, expected) in enumerate(cases, 1):
            result = subprocess.run(
                ["java", "-cp", build_dir, "janet.Janet"], cwd=ROOT,
                input=inputs, text=True, capture_output=True, check=False
            )
            actual = result.stdout.replace("\r\n", "\n")
            expected = expected.replace("\r\n", "\n")
            print(f"=== Test {index}: {name} ===")
            print(f"Aim: {aim}")
            print("--- Console input ---")
            print(inputs, end="" if inputs.endswith("\n") else "\n")
            print("--- Console output ---")
            print(actual, end="" if actual.endswith("\n") else "\n")
            if result.returncode != 0 or actual != expected:
                print("--- Expected output ---")
                print(expected, end="" if expected.endswith("\n") else "\n")
                print(f"FAILED: test {index} ({name})")
                return 1
            print("PASSED\n")
    print(f"All {len(cases)} UI tests passed.")
    return 0


if __name__ == "__main__":
    sys.exit(run())
