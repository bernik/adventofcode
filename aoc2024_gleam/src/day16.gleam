import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option.{Some}
import gleam/set
import gleam/string
import utils

type Pos =
  #(Int, Int)

type Dir =
  #(Int, Int)

type Map =
  dict.Dict(Pos, String)

type Scores =
  dict.Dict(Pos, Int)

fn parse_input(file) -> Map {
  utils.read_lines(file)
  |> list.index_map(fn(line, row) {
    line
    |> string.split(on: "")
    |> list.index_map(fn(x, col) { #(#(row, col), x) })
  })
  |> list.flatten
  |> dict.from_list
}

fn find_node(map: Map, needle: String) -> Pos {
  let assert Ok(#(pos, _)) =
    map
    |> dict.to_list
    |> list.find(fn(node) { node.1 == needle })

  pos
}

fn neighbours(map: Map, pos: Pos) -> List(#(Pos, Dir)) {
  [#(0, 1), #(0, -1), #(1, 0), #(-1, 0)]
  |> list.filter(fn(x) { x.0 + pos.0 != 0 || x.1 + pos.1 != 0 })
  |> list.map(fn(x) { #(#(pos.0 + x.0, pos.1 + x.1), x) })
  |> list.filter(fn(x) { dict.get(map, x.0) != Ok("#") })
}

fn do_best_score(
  map: dict.Dict(Pos, String),
  queue: List(#(Pos, Dir)),
  visited: set.Set(Pos),
  scores: Scores,
) -> Scores {
  case queue {
    [] -> scores
    [#(cur_pos, cur_dir), ..queue] -> {
      case set.contains(visited, cur_pos) {
        True -> do_best_score(map, queue, visited, scores)

        False -> {
          let assert Ok(cur_score) = dict.get(scores, cur_pos)
          let neighbours = neighbours(map, cur_pos)
          let new_scores =
            neighbours
            |> list.fold(scores, fn(scores, n) {
              let #(n_pos, n_dir) = n
              let n_score = case n_dir == cur_dir {
                True -> cur_score + 1
                False -> cur_score + 1001
              }

              scores
              |> dict.upsert(n_pos, fn(x) {
                case x {
                  Some(score) if n_score < score -> n_score
                  Some(score) -> score
                  _ -> n_score
                }
              })
            })

          let new_queue =
            list.append(queue, neighbours)
            |> list.sort(fn(a, b) {
              let assert Ok(a_score) = new_scores |> dict.get(a.0)
              let assert Ok(b_score) = new_scores |> dict.get(b.0)
              int.compare(a_score, b_score)
            })

          do_best_score(
            map,
            new_queue,
            set.insert(visited, cur_pos),
            new_scores,
          )
        }
      }
    }
  }
}

/// for debug
// fn print_map(map: Map, visited: set.Set(Pos)) -> Nil {
//   let height = map |> dict.keys |> list.map(pair.first) |> list.fold(0, int.max)
//   let width = map |> dict.keys |> list.map(pair.second) |> list.fold(0, int.max)

//   list.range(0, height)
//   |> list.each(fn(row) {
//     list.range(0, width)
//     |> list.map(fn(col) {
//       case set.contains(visited, #(row, col)) {
//         True -> "0"
//         False ->
//           case dict.get(map, #(row, col)) {
//             Ok(v) -> v
//             _ -> " "
//           }
//       }
//     })
//     |> string.concat
//     |> io.println
//   })
// }

fn best_score(map: Map) -> Int {
  let start = find_node(map, "S")
  let end = find_node(map, "E")
  let assert Ok(score) =
    do_best_score(
      map,
      [#(start, #(0, 1))],
      set.new(),
      dict.from_list([#(start, 0)]),
    )
    |> dict.get(end)

  score
}

fn part1(file) {
  let map = parse_input(file)
  let score = best_score(map)
  int.to_string(score)
}

fn do_part2(
  map: Map,
  target: Int,
  score: Int,
  dir: Dir,
  path: List(Pos),
  visited: set.Set(Pos),
) -> List(List(Pos)) {
  let assert Ok(cur) = list.first(path)
  // print_map(map, visited)
  // case int.random(1_000_000) {
  //   0 -> {
  //     io.print("\u{001b}[2J")
  //     print_map(map, visited)
  //     process.sleep(500)
  //   }
  //   _ -> Nil
  // }
  case set.contains(visited, cur) || score > target {
    True -> []
    False -> {
      case dict.get(map, cur) {
        Ok("E") if score == target -> [path]
        Ok("E") -> []
        _ -> {
          let neighbours = neighbours(map, cur)
          neighbours
          |> list.flat_map(fn(n) {
            let #(n_pos, n_dir) = n
            let new_score = case dir == n_dir {
              True -> score + 1
              False -> score + 1001
            }
            do_part2(
              map,
              target,
              new_score,
              n_dir,
              [n_pos, ..path],
              set.insert(visited, cur),
            )
          })
        }
      }
    }
  }
}

fn part2(file) {
  let map = parse_input(file)
  let start = find_node(map, "S")
  let best = best_score(map)

  do_part2(map, best, 0, #(0, 1), [start], set.new())
  |> list.flatten
  |> set.from_list
  |> set.size
  |> int.to_string
}

pub fn main() {
  let assert "7036" = part1("./resources/day16.example.txt")
  let assert "11048" = part1("./resources/day16.example2.txt")

  let assert "45" = part2("./resources/day16.example.txt")
  let assert "64" = part2("./resources/day16.example2.txt")

  let file = "./resources/day16.input.txt"
  let part1 = part1(file)
  io.println("Day 16 | Part 1: " <> part1)
  // @todo not working
  // let part2 = part2(file)
  // 
  // 513 < part2 
  // io.println("Day 16 | Part 2: " <> part2)
}
