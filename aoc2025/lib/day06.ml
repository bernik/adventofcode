let parse_input file =
  let lines = open_in file |> In_channel.input_lines in
  let len = List.length lines in
  let blocks = List.take (len - 1) lines in
  List.drop (len - 1) lines
  |> List.hd
  |> Utils.match_all {|[\*\+] *|}
  |> List.map (fun (op, a, b) ->
    let numbers = blocks |> List.map (fun line -> String.sub line a (b - a + 1)) in
    op, numbers)
;;

let int_of_string = Fun.compose int_of_string String.trim

let part1 file =
  parse_input file
  |> List.map (fun (op, numbers) ->
    match op.[0] with
    | '+' -> numbers |> List.map int_of_string |> List.fold_left ( + ) 0
    | '*' -> numbers |> List.map int_of_string |> List.fold_left ( * ) 1
    | _ -> 0)
  |> List.fold_left ( + ) 0
;;

let part2 file =
  parse_input file
  |> List.map (fun (op, numbers) ->
    let numbers' =
      op
      |> String.to_seq
      |> Seq.mapi (fun i _ ->
        numbers |> List.map (fun s -> s.[i]) |> List.to_seq |> String.of_seq |> String.trim)
      |> Seq.filter (fun x -> x <> "")
      |> Seq.map int_of_string
    in
    match op.[0] with
    | '+' -> numbers' |> Seq.fold_left ( + ) 0
    | '*' -> numbers' |> Seq.fold_left ( * ) 1
    | _ -> 0)
  |> List.fold_left ( + ) 0
;;
