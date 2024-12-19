import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/regexp
import gleam/result
import gleam/string
import pprint.{debug as d}
import simplifile
import utils

type Instruction {
  Adv(Int)
  Bxl(Int)
  Bst(Int)
  Jnz(Int)
  Bxc
  Out(Int)
  Bdv(Int)
  Cdv(Int)
}

type Program =
  dict.Dict(Int, Instruction)

fn parse_program(input: List(Int)) -> Program {
  input
  |> list.sized_chunk(2)
  |> list.index_map(fn(chunk, idx) {
    let assert [i, op] = chunk
    let instruction = case i {
      0 -> Adv(op)
      1 -> Bxl(op)
      2 -> Bst(op)
      3 -> Jnz(op)
      4 -> Bxc
      5 -> Out(op)
      6 -> Bdv(op)
      7 -> Cdv(op)
      _ -> panic as "Invalid instruction"
    }
    #(idx * 2, instruction)
  })
  |> dict.from_list
}

fn parse_input(file) {
  let assert Ok(content) = simplifile.read(file)
  let assert Ok(#(registers, program_line)) =
    string.split_once(content, on: "\n\n")
  let assert Ok(re) = regexp.from_string("\\d+")

  let assert [a, b, c] =
    registers
    |> regexp.scan(re, _)
    |> list.map(fn(m) {
      let regexp.Match(n, _) = m
      utils.string_to_int(n)
    })

  let registers = dict.from_list([#("A", a), #("B", b), #("C", c)])

  let program_input =
    program_line
    |> regexp.scan(re, _)
    |> list.map(fn(m) {
      let regexp.Match(n, _) = m
      utils.string_to_int(n)
    })

  let program = parse_program(program_input)

  #(registers, program, program_input)
}

fn run(
  program: Program,
  registers: dict.Dict(String, Int),
  pointer: Int,
  out: List(Int),
) -> String {
  let a = fn() { registers |> dict.get("A") |> result.unwrap(0) }
  let b = fn() { registers |> dict.get("B") |> result.unwrap(0) }
  let c = fn() { registers |> dict.get("C") |> result.unwrap(0) }

  let combo = fn(op) {
    case op {
      4 -> a()
      5 -> b()
      6 -> c()
      7 -> panic as "Invalid operand"
      _ -> op
    }
  }

  case dict.get(program, pointer) {
    Ok(instruction) -> {
      case instruction {
        Adv(op) ->
          run(
            program,
            registers
              |> dict.insert("A", int.bitwise_shift_right(a(), combo(op))),
            pointer + 2,
            out,
          )

        Bxl(op) ->
          run(
            program,
            registers |> dict.insert("B", int.bitwise_exclusive_or(b(), op)),
            pointer + 2,
            out,
          )
        Bst(op) ->
          run(
            program,
            registers |> dict.insert("B", int.bitwise_and(combo(op), 7)),
            pointer + 2,
            out,
          )

        Jnz(op) ->
          case a() == 0 {
            True -> run(program, registers, pointer + 2, out)
            False -> run(program, registers, op, out)
          }
        Bxc ->
          run(
            program,
            registers |> dict.insert("B", int.bitwise_exclusive_or(b(), c())),
            pointer + 2,
            out,
          )

        Out(op) ->
          run(program, registers, pointer + 2, [
            int.bitwise_and(combo(op), 7),
            ..out
          ])
        Bdv(op) ->
          run(
            program,
            registers
              |> dict.insert("B", int.bitwise_shift_right(a(), combo(op))),
            pointer + 2,
            out,
          )

        Cdv(op) ->
          run(
            program,
            registers
              |> dict.insert("C", int.bitwise_shift_right(a(), combo(op))),
            pointer + 2,
            out,
          )
      }
    }
    Error(Nil) ->
      out |> list.reverse |> list.map(int.to_string) |> string.join(with: ",")
  }
}

fn part1(file) {
  let #(registers, program, _) = parse_input(file)

  run(program, registers, 0, [])
}

fn step(a) {
  let b = int.bitwise_and(a, 7)
  let b = int.bitwise_exclusive_or(b, 5)
  let c = int.bitwise_shift_right(a, b)
  let b = int.bitwise_exclusive_or(b, 6)
  let b = int.bitwise_exclusive_or(b, c)
  int.bitwise_and(b, 7)
}

fn run2(a, output) {
  case a == 0 {
    True ->
      output |> list.reverse |> list.map(int.to_string) |> string.join(",")

    False -> run2(int.bitwise_shift_right(a, 3), [step(a), ..output])
  }
}

fn do_part2(a, input) {
  case input {
    [] -> Ok(a)
    [b, ..input] -> {
      let a = int.bitwise_shift_left(a, 3)

      list.range(a, a + 7)
      |> list.filter_map(fn(a) {
        case step(a) == b {
          True -> do_part2(a, input)
          False -> Error(Nil)
        }
      })
      |> list.sort(int.compare)
      |> list.first
    }
  }
}

fn part2(file) {
  let #(_, _, program_input) = parse_input(file)
  let assert Ok(a) = do_part2(0, program_input |> list.reverse)

  int.to_string(a)
}

fn run_examples() {
  let registers = dict.from_list([#("A", 10)])
  let program = parse_program([5, 0, 5, 1, 5, 4])
  let assert "0,1,2" = run(program, registers, 0, [])

  let registers = dict.from_list([#("A", 2024)])
  let program = parse_program([0, 1, 5, 4, 3, 0])
  let assert "4,2,5,6,7,7,7,7,3,1,0" = run(program, registers, 0, [])

  let program = parse_program([2, 4, 1, 5, 7, 5, 1, 6, 4, 2, 5, 5, 0, 3, 3, 0])
  run(program, registers |> dict.insert("A", 120_397_325_992_218), 0, [])
}

pub fn main() {
  run_examples()
  let assert "4,6,3,5,6,3,5,2,1,0" = part1("./resources/day17.example.txt")

  let file = "./resources/day17.input.txt"
  let part1 = part1(file)
  io.println("Day 17 | Part 1: " <> part1)
  let part2 = part2(file)
  io.println("Day 17 | Part 2: " <> part2)
}
