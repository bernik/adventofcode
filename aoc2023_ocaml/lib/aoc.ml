open Base 

let print_string_list lst =
    lst 
    |> List.map ~f:(fun x -> "\"" ^ x ^ "\"")
    |> String.concat ~sep:", \n"
    |> Stdio.printf "[%s] \n"
;;

let print_int_list lst =
    lst
    |> List.map ~f:Int.to_string
    |> String.concat ~sep:", "
    |> Stdio.printf "[%s]\n"
;;

let print_int_arr arr =
    arr
    |> Array.to_list
    |> List.mapi ~f:(fun idx n -> Printf.sprintf "%d => %d" idx n)
    |> String.concat ~sep:", \n"
    |> Stdio.printf "(%s)\n"
;;

