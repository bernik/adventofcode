let parse_input file =
  let input = open_in file in
  let raw = In_channel.input_all input in
  close_in input;
  Utils.match_all "[0-9]+" raw
  |> List.map (fun (s, _, _) -> s)
  |> List.fold_left
       (fun (acc, prev) n ->
          match prev with
          | None -> acc, Some (int_of_string n)
          | Some prev -> (prev, int_of_string n) :: acc, None)
       ([], None)
  |> fst
;;

let range from' to' =
  let rec aux acc = function
    | n when n < from' -> acc
    | n -> aux (n :: acc) (n - 1)
  in
  aux [] to'
;;

let part1 file =
  parse_input file
  |> List.fold_left
       (fun acc (from', to') ->
          acc
          + (range from' to'
             |> List.filter (fun n ->
               let re = Str.regexp {| ^\([0-9]+\)\1$ |} in
               Str.string_match re (string_of_int n) 0)
             |> List.fold_left ( + ) 0))
       0
;;

let part2 file =
  parse_input file
  |> List.fold_left
       (fun acc (from', to') ->
          acc
          + (range from' to'
             |> List.filter (fun n ->
               let re = Str.regexp "^\\([0-9]+\\)\\1+$" in
               Str.string_match re (string_of_int n) 0)
             |> List.fold_left ( + ) 0))
       0
;;
