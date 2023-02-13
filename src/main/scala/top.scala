import chisel3._
import chisel3.util._

object top extends App {
  emitVerilog(new top, Array("--target-dir", "generated"))
}

class top extends Module {
  val io = IO(new Bundle {
    val in = Input(Bool())
    val out = Output(Bool())
    val status = Output(UInt(4.W))
  })

  val statusReg = RegInit(0.U(4.W))
  val outReg = RegInit(false.B)

  io.status := statusReg
  io.out := outReg
  
  when (io.in) {
    when (statusReg === 0.U || statusReg === 1.U || statusReg === 2.U || statusReg === 3.U || statusReg === 4.U) {
      statusReg := 5.U
      outReg := false.B
    } .elsewhen (statusReg === 5.U) {
      statusReg := 6.U
      outReg := false.B
    } .elsewhen (statusReg === 6.U) {
      statusReg := 7.U
      outReg := false.B
    } .elsewhen (statusReg === 7.U) {
      statusReg := 8.U
      outReg := true.B
    } .elsewhen (statusReg === 8.U) {
      statusReg := 8.U
      outReg := true.B
    }
  } .otherwise {
    when (statusReg === 0.U || statusReg === 5.U || statusReg === 6.U || statusReg === 7.U || statusReg === 8.U) {
      statusReg := 1.U
      outReg := false.B
    } .elsewhen (statusReg === 1.U) {
      statusReg := 2.U
      outReg := false.B
    } .elsewhen (statusReg === 2.U) {
      statusReg := 3.U
      outReg := false.B
    } .elsewhen (statusReg === 3.U) {
      statusReg := 4.U
      outReg := true.B
    } .elsewhen (statusReg === 4.U) {
      statusReg := 4.U
      outReg :=  true.B
    }
  }
}
