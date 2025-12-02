let read_lines file f =
  let input = open_in file in
  let lines = In_channel.input_lines input in
  List.map f lines
;;

let match_all pattern s =
  let re = Str.regexp pattern in
  let rec matches acc start =
    try
      let _ = Str.search_forward re s start in
      let res = Str.matched_string s, Str.match_beginning (), Str.match_end () - 1 in
      matches (res :: acc) (Str.match_end ())
    with
    | _ -> List.rev acc
  in
  matches [] 0
;;
