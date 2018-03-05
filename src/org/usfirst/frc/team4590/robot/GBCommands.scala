package org.usfirst.frc.team4590.robot

import edu.wpi.first.wpilibj.command.Command

case class Delayed(comm : Command, delay : Long) extends Command(delay / 1000d){
  def isFinished = isTimedOut
  override def end = GBScheduler <= comm
}



case class ChoiceCondition(first : Command, second : Command, condition : () => Boolean) extends Command{
  def this(first : Command, second : Command, condition : Boolean) = this(first, second, () => condition)
  
  override def execute = GBScheduler <= (if (condition()) first else second)
  override def isFinished = true
}


