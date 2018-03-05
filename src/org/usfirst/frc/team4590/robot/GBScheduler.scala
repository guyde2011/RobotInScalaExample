package org.usfirst.frc.team4590.robot

import edu.wpi.first.wpilibj.command._

object GBScheduler {
  protected def scheduleCommand(comm : Command) : Boolean = { 
    if (shouldSchedule(comm)){
      Scheduler.getInstance().add(comm)
      return true
    } 
    return false
  } 
 
  protected def scheduleModifier(mod : CommandModifier) : Boolean = {
    mod match {
      case null => return false  
      case Conditional(com, condition) if condition() => return scheduleCommand(com)  
      case any => return scheduleCommand(any.command)
    }
    return false
  }
  
  protected def shouldSchedule(comm : Command) : Boolean = {
    true
  }
  
  def run() = {
    Scheduler.getInstance.run
  }
  
  
  implicit def comToModifer(com : Command) = NoModifier(com)
  
  def <= (comm : CommandModifier) : Boolean = scheduleModifier(comm)
  
  sealed trait CommandModifier{
    def command : Command
  }
  
  case class Conditional(command : Command, condition : () => Boolean) extends CommandModifier
  
  
  object Conditional{
    def apply(command : Command, conditional : Boolean) = new Conditional(command, () => conditional)
  }
 
  case class NoModifier(command : Command) extends CommandModifier
  
}