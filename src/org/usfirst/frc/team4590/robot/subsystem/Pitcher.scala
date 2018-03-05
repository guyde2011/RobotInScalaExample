package org.usfirst.frc.team4590.robot.subsystem

import edu.wpi.first.wpilibj.command.Subsystem
import org.usfirst.frc.team4590.robot.subsystem.Subsystems.GBSubsystem
import edu.wpi.first.wpilibj.AnalogPotentiometer
import org.usfirst.frc.team4590.robot.commands.PitcherCommands.State._

import org.usfirst.frc.team4590.robot.TypeSubs._
import org.usfirst.frc.team4590.robot.commands.Commands

object PitcherSubsystem extends GBSubsystem("Pitcher") {
  
  private val Command = Commands.Pitcher
  
  private var talon : SmartTalon = null
  private var potentiometer : AnalogPotentiometer = null
  
  private var _UP : Double = -1
  private var _DOWN : Double = -1
  
  val equalibriumState : Double = 0
  
  def init() {
   talon = new SmartTalon(0)
   potentiometer = new AnalogPotentiometer(0)
   _UP = potentiometer.get()
   _DOWN = _UP - 0.693
  }
  
  def update() {
    SD.putNumber("Number", 5)
  }
  
  def upState = _UP
  def downState = _DOWN
  
  private var lastPower : Double = 0
  
  def state : PitcherState = Position((potentiometer.get() - downState) / (upState - downState))
  
  def position : Double = state.position
  
  def angle : Double = state.angle
  
  def initDefaultCommand = setDefaultCommand(Command.HoldPitcher(Backward))
  
  def power : Double = lastPower
  
  def power_=(power : Double) = {lastPower = power; talon.set(power)}
  
  def stop = power = 0
}