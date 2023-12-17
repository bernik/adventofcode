open Base 
open Stdio

module Data =  struct  

    type t = int array array;;

    let of_string s = 
        String.split_lines s 
        |> List.to_array
        |> Array.map ~f:(fun line -> 
            String.to_array line 
            |> Array.map ~f:(function | '.' -> 0 | _ -> 1)
        )
    ;;

    let print = 
        Array.iter ~f:(fun row -> 
            printf "%s\n" (Array.map row ~f:Int.to_string |> String.concat_array) 
        ) 
    ;;

    let find_mid t = 
        let len = Array.length t in
        List.range 1 (len - 1) 
        |> List.find ~f:(fun n -> false)

end 


let parse file = 
    In_channel.read_all file
    |> Str.split (Str.regexp "\n\n")
    |> List.map ~f:Data.of_string
;;


let () = 
    let data = parse "day13/input.example.txt" |> List.hd_exn in
    Data.print data;
    print_endline "-----";
    data |> Array.transpose_exn |> Data.print;
;;



let part1 file = "part1";;
let part2 file = "part2";;


let () = 
    printf "part1 example: %s\n" (part1 "day13/input.example.txt");
    printf "part1: %s\n"         (part1 "day13/input.txt");
    printf "part2 example: %s\n" (part2 "day13/input.example.txt");
    printf "part2: %s\n"         (part2 "day13/input.txt");
    ();

