import gleam/int
import gleam/io
import gleam/list
import gleam/pair
import gleam/string
import simplifile
import utils

fn parse_input(file) {
  let assert Ok(content) = simplifile.read(file)
  content
  |> string.split(on: "")
  |> list.map(utils.string_to_int)
  |> list.sized_chunk(into: 2)
  |> list.index_fold([], fn(acc, chunk, idx) {
    case chunk {
      [n] -> list.append(acc, list.repeat(idx, times: n))

      [n, free] ->
        acc
        |> list.append(list.repeat(idx, times: n))
        |> list.append(list.repeat(-1, times: free))

      _ -> acc
    }
  })
}

type Slot {
  File(index: Int, size: Int)
  Space(size: Int)
}

fn parse_input2(file) {
  let assert Ok(content) = simplifile.read(file)
  content
  |> string.split(on: "")
  |> list.index_map(fn(n, i) {
    case i % 2 {
      0 -> File(i / 2, utils.string_to_int(n))
      _ -> Space(utils.string_to_int(n))
    }
  })
}

fn do_defrag(acc, xs) {
  let first = list.first(xs)
  let last = list.last(xs)
  let l = list.length(xs)
  case first, last {
    Error(Nil), _ -> list.reverse(acc)
    _, Ok(-1) -> do_defrag(acc, list.take(xs, l - 1))
    Ok(-1), Ok(n) ->
      do_defrag([n, ..acc], xs |> list.take(l - 1) |> list.drop(1))
    Ok(n), _ -> do_defrag([n, ..acc], xs |> list.drop(1))
  }
}

fn part1(file) {
  let xs = parse_input(file)
  do_defrag([], xs)
  |> list.index_fold(0, fn(acc, n, idx) { acc + n * idx })
  |> int.to_string
}

fn print_slots(slots) {
  slots
  |> list.each(fn(slot) {
    case slot {
      File(i, s) -> io.print(i |> int.to_string |> string.repeat(s))
      Space(s) -> io.print(string.repeat(".", s))
    }
  })
}

fn has_free_slot(slots, file, i) {
  case slots, file {
    [], _ -> Error(Nil)
    [File(i, _), ..], File(ii, _) if i == ii -> Error(Nil)
    [Space(free), ..], File(_, size) if free >= size -> Ok(i)
    [_, ..rest], _ -> has_free_slot(rest, file, i + 1)
  }
}

fn move_file(slots, file_index, file_size, to) {
  slots
  |> list.index_map(fn(slot, i) {
    case slot, i {
      Space(n), i if i == to -> {
        case n == file_size {
          True -> [File(file_index, file_size)]
          False -> [File(file_index, file_size), Space(n - file_size)]
        }
      }
      File(i, _), _ if i == file_index -> {
        [Space(file_size)]
      }

      _, _ -> [slot]
    }
  })
  |> list.flatten
}

fn do_part2(slots, files) {
  case files {
    [] -> slots
    [File(_, _) as f, ..files] -> {
      case has_free_slot(slots, f, 0) {
        Ok(slot_idx) -> {
          slots |> move_file(f.index, f.size, slot_idx) |> do_part2(files)
        }

        _ -> do_part2(slots, files)
      }
    }
    _ -> panic
  }
}

fn part2(file) {
  let slots = parse_input2(file)
  let files =
    slots
    |> list.filter(fn(x) {
      case x {
        File(_, _) -> True
        Space(_) -> False
      }
    })
    |> list.reverse
  do_part2(slots, files)
  |> list.fold(#(0, 0), fn(acc, slot) {
    let #(res, idx) = acc
    case slot {
      Space(size) -> #(res, idx + size)
      File(i, size) -> {
        let checksum =
          list.range(idx, idx + size - 1) |> int.sum |> int.multiply(i)
        #(res + checksum, idx + size)
      }
    }
  })
  |> pair.first
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day09.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 09 | Part 1: " <> part1)
  io.println("Day 09 | Part 2: " <> part2)
}
