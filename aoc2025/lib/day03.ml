let parse_input file =
  Utils.read_lines file (fun line -> line |> String.to_seq |> Seq.map Char.escaped |> List.of_seq)
;;

let part1 file =
  let input = parse_input file in
  let rec aux acc = function
    | [] | [ _ ] -> acc
    | x :: xs ->
      let max' = xs |> List.fold_left (fun acc y -> max acc (int_of_string (x ^ y))) 0 in
      aux (max acc max') xs
  in
  input |> List.map (aux 0) |> List.fold_left ( + ) 0
;;

let part2 file =
  let input = parse_input file in
  let rec aux found xs =
    let found_len = List.length found in
    let xs_len = List.length xs in
    if found_len = 12
    then List.rev found
    else (
      let part = xs |> List.take (xs_len - (12 - found_len) + 1) in
      let i, n =
        part
        |> List.mapi (fun i n -> i, n)
        |> List.fold_left
             (fun (acc_i, acc_n) (i, n) ->
                if int_of_string n > int_of_string acc_n then i, n else acc_i, acc_n)
             (-1, "0")
      in
      aux (n :: found) (List.drop (i + 1) xs))
  in
  input
  |> List.map (fun xs -> xs |> aux [] |> String.concat "" |> int_of_string)
  |> List.fold_left ( + ) 0
;;
