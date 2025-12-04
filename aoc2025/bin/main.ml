[@@@warning "-32"]

open Aoc2025

let day01 () =
  Fmt.pr
    "day 01\n part 1: %d, %d\n part 2: %d, %d\n"
    (Day01.part1 "resources/day01.example.txt")
    (Day01.part1 "resources/day01.input.txt")
    (Day01.part2 "resources/day01.example.txt")
    (Day01.part2 "resources/day01.input.txt")
;;

let day02 () =
  Fmt.pr
    "day 02\n part 1: %d, %d\n part 2: %d, %d\n"
    (Day02.part1 "resources/day02.example.txt")
    (Day02.part1 "resources/day02.input.txt")
    (Day02.part2 "resources/day02.example.txt")
    (Day02.part2 "resources/day02.input.txt")
;;

let day03 () =
  Fmt.pr
    "day 03\n part 1: %d, %d\n part 2: %d, %d\n"
    (Day03.part1 "resources/day03.example.txt")
    (Day03.part1 "resources/day03.input.txt")
    (Day03.part2 "resources/day03.example.txt")
    (Day03.part2 "resources/day03.input.txt")
;;

let day04 () =
  Fmt.pr
    "day 04\n part 1: %d, %d\n part 2: %d, %d\n"
    (Day04.part1 "resources/day04.example.txt")
    (Day04.part1 "resources/day04.input.txt")
    (Day04.part2 "resources/day04.example.txt")
    (Day04.part2 "resources/day04.input.txt")
;;

let () =
  (* day01 ();
  day02 ();
  day03 () *)
  day04 ()
;;
