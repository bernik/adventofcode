[@@@warning "-32"]

let () =
  Fmt.pr
    "day 05\n part 1: %d, %d\n part 2: %d, %d\n"
    (Aoc2025.Day05.part1 "resources/day05.example.txt")
    (Aoc2025.Day05.part1 "resources/day05.input.txt")
    (Aoc2025.Day05.part2 "resources/day05.example.txt")
    (Aoc2025.Day05.part2 "resources/day05.input.txt")
;;
