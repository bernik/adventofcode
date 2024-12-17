import gleam/dict
import gleam/float
import gleam/int
import gleam/io
import gleam/list
import gleam/regexp
import gleam/result
import gleam/string
import gleam/yielder
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

fn parse_program(input: String) -> Program {
  let assert Ok(re) = regexp.from_string("\\d+")

  input
  |> regexp.scan(re, _)
  |> list.sized_chunk(2)
  |> list.index_map(fn(chunk, idx) {
    let assert [regexp.Match(i, _), regexp.Match(op, _)] = chunk
    let op = utils.string_to_int(op)
    let instruction = case i {
      "0" -> Adv(op)
      "1" -> Bxl(op)
      "2" -> Bst(op)
      "3" -> Jnz(op)
      "4" -> Bxc
      "5" -> Out(op)
      "6" -> Bdv(op)
      "7" -> Cdv(op)
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

  let assert Ok(#(_, program_raw)) = string.split_once(program_line, on: " ")
  let program = parse_program(program_line)

  #(registers, program, program_raw)
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
  let instruction = dict.get(program, pointer)
  // d(instruction)

  case instruction {
    Ok(instruction) -> {
      case instruction {
        Adv(op) -> {
          let assert Ok(den) =
            int.power(2, combo(op) |> int.to_float)
            |> result.map(float.truncate)

          let res = a() / den
          run(program, registers |> dict.insert("A", res), pointer + 2, out)
        }
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
            registers |> dict.insert("B", combo(op) % 8),
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

        Out(op) -> run(program, registers, pointer + 2, [combo(op) % 8, ..out])
        Bdv(op) -> {
          let assert Ok(den) =
            int.power(2, combo(op) |> int.to_float)
            |> result.map(float.truncate)

          let res = a() / den
          run(program, registers |> dict.insert("B", res), pointer + 2, out)
        }
        Cdv(op) -> {
          let assert Ok(den) =
            int.power(2, combo(op) |> int.to_float)
            |> result.map(float.truncate)

          let res = a() / den
          run(program, registers |> dict.insert("C", res), pointer + 2, out)
        }
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

// todo inverse run 
fn part2(file) {
  let #(registers, program, program_string) = parse_input(file)
  yielder.iterate(0, int.add(_, 1))
  |> yielder.drop_while(fn(n) {
    let res = run(program, registers |> dict.insert("A", n), 0, [])
    d(#(n, res))
    res != program_string
  })
  |> yielder.first
  |> result.unwrap(0)
  |> int.to_string
}

fn run_examples() {
  let registers = dict.from_list([#("A", 10)])
  let program = parse_program("5,0,5,1,5,4")
  let assert "0,1,2" = run(program, registers, 0, [])

  let registers = dict.from_list([#("A", 2024)])
  let program = parse_program("0,1,5,4,3,0")
  let assert "4,2,5,6,7,7,7,7,3,1,0" = run(program, registers, 0, [])
  Nil
}

pub fn main() {
  run_examples()
  let assert "4,6,3,5,6,3,5,2,1,0" = part1("./resources/day17.example.txt")

  let file = "./resources/day17.input.txt"
  let part1 = part1(file)
  io.println("Day 17 | Part 1: " <> part1)

  // let part2 = part2(file)
  // io.println("Day 17 | Part 2: " <> part2)
  // parse_program("0,3,5,4,3,0") |> dict.values |> d
  // let #(registers, program, program_string) = parse_input(file)
  // program |> dict.values |> d
  Nil
}
