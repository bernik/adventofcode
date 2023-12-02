open Core 


let read_lines file = 
    file |> In_channel.read_lines
