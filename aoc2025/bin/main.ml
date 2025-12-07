[@@@warning "-32"]

let () =
  Fmt.pr
    "part 1: %d, %d\npart 2: %d, %d\n"
    (Aoc2025.Day07.part1 "resources/day07.example.txt")
    (Aoc2025.Day07.part1 "resources/day07.input.txt")
    (Aoc2025.Day07.part2 "resources/day07.example.txt")
    (Aoc2025.Day07.part2 "resources/day07.input.txt")
;;
(* 0 *)
