[@@@warning "-32"]

let () =
  Fmt.pr
    "part 1: %d, %d\npart 2: %d, %d\n"
    (Aoc2025.Day08.part1 "resources/day08.example.txt" 10)
    (Aoc2025.Day08.part1 "resources/day08.input.txt" 1000)
    (Aoc2025.Day08.part2 "resources/day08.example.txt")
    (Aoc2025.Day08.part2 "resources/day08.input.txt")
;;
(* 0 *)
