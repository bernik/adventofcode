let parse_input file =
  let lines = open_in file |> In_channel.input_lines in
  let ranges =
    lines
    |> List.take_while (fun l -> l <> "")
    |> List.map (fun line ->
      match String.split_on_char '-' line with
      | [ a; b ] -> int_of_string a, int_of_string b
      | _ -> failwith ("invalid line" ^ line))
  in
  let xs = lines |> List.drop (List.length ranges + 1) |> List.map int_of_string in
  ranges, xs
;;

let part1 file =
  let ranges, xs = parse_input file in
  xs
  |> List.filter_map (fun n -> ranges |> List.find_opt (fun (a, b) -> a <= n && n <= b))
  |> List.length
;;

let overlap (a1, b1) (a2, b2) = a1 <= b2 && a2 <= b1

let part2 file =
  parse_input file
  |> fst
  |> List.sort (fun (a1, b1) (a2, b2) ->
    match compare a1 a2 with
    | 0 -> compare b1 b2
    | n -> n)
  |> List.fold_left
       (fun acc range ->
          if List.exists (overlap range) acc
          then
            acc
            |> List.map (fun x ->
              if overlap range x then min (fst x) (fst range), max (snd range) (snd x) else x)
          else range :: acc)
       []
  |> List.fold_left (fun acc (a, b) -> acc + b - a + 1) 0
;;
