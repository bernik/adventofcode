open Aoc2025

let () =
  Fmt.pr
    "day 01\n part 1: %d, %d\n part 2: %d, %d\n"
    (Day01.part1 "resources/day01.example.txt")
    (Day01.part1 "resources/day01.input.txt")
    (Day01.part2 "resources/day01.example.txt")
    (Day01.part2 "resources/day01.input.txt");
  Fmt.pr
    "day 02\n part 1: %d, %d\n part 2: %d, %d\n"
    (Day02.part1 "resources/day02.example.txt")
    (Day02.part1 "resources/day02.input.txt")
    (Day02.part2 "resources/day02.example.txt")
    (Day02.part2 "resources/day02.input.txt")
;;
