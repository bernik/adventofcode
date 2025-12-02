let parse_input file =
  Utils.read_lines file (fun l ->
    let len = String.length l in
    let dir = l.[0] in
    let amount = int_of_string (String.sub l 1 (len - 1)) in
    dir, amount)
;;

let part1 file =
  let lines = parse_input file in
  let _, result =
    lines
    |> List.fold_left
         (fun (acc, res) ->
            fun (dir, n) ->
            let acc =
              match dir, n with
              | 'R', n -> (acc + n) mod 100
              | 'L', n when acc >= n -> acc - n
              | 'L', n -> (100 + acc - n) mod 100
              | _ -> acc
            in
            let res = if acc = 0 then res + 1 else res in
            acc, res)
         (50, 0)
  in
  result
;;

let brute_force start steps dir =
  let rec aux acc current steps =
    if steps = 0
    then acc
    else (
      let current' = (current + dir) mod 100 in
      let acc' = acc + if current' = 0 then 1 else 0 in
      aux acc' current' (steps - 1))
  in
  aux 0 start steps
;;

let part2 file =
  let lines = parse_input file in
  let _, result =
    lines
    |> List.fold_left
         (fun (acc, res) (dir, n) ->
            let new_acc, cnt =
              match dir, n with
              | 'R', n -> (acc + n) mod 100, brute_force acc n 1
              | 'L', n when acc > n -> acc - n, 0
              | 'L', n -> (100 + acc - n) mod 100, brute_force acc n (-1)
              | _ -> acc, n
            in
            new_acc, res + cnt)
         (50, 0)
  in
  result
;;
