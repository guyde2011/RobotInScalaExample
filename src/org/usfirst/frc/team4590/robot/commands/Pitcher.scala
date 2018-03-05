package org.usfirst.frc.team4590.robot.commands

import org.usfirst.frc.team4590.robot.RobotMap
import edu.wpi.first.wpilibj.command.Command
import org.usfirst.frc.team4590.robot.subsystem.Subsystems._
import org.usfirst.frc.team4590.robot._

import org.usfirst.frc.team4590.robot.GBScheduler._


object PitcherCommands {
  
  object State{
    
    sealed trait PitcherState{
      def position : Double
      def angle : Double = position * 180
    }
    
    object Collect extends PitcherState{ def position = RobotMap.Pitcher.collect}
    
    object Forward extends PitcherState{ def position = RobotMap.Pitcher.forward}
    
    object Backward extends PitcherState{ def position = RobotMap.Pitcher.backward}
    
    object Plate extends PitcherState{ def position = RobotMap.Pitcher.plate}
    
    object Exchange extends PitcherState{ def position = RobotMap.Pitcher.exchange}
    
    case class Angle(override val angle : Double) extends PitcherState{ def position = angle / 180d }
    
    case class Position(position : Double) extends PitcherState
    
    object Of{
      object Pos{
        def unapply(state : PitcherState) : Option[Double] = if (state == null) None else Some(state.position)      
      }
      object Angle{
        def unapply(state : PitcherState) : Option[Double] = if (state == null) None else Some(state.angle)      
      }
      
      object Relaxed{
        val tolerance = 3d
        
        def unapply(state : PitcherState) : Boolean = (Plate.angle - state.angle).abs <= tolerance || (Collect.angle - state.angle).abs <= tolerance
      }
    }
    
    
    
  }
  
  sealed trait PitcherCommand{
    this : Command =>
      
    def state : State.PitcherState 
    
    def position = state.position
    def angle = state.angle
    
    def tolerance = 5/180d 
  }
  
  case class MovePitcherToState(state : State.PitcherState) extends Command with PitcherCommand{
    
    requires(Pitcher)
    
    def isFinished = (position - Pitcher.position).abs <= tolerance
    
    private val DECEL_RANGE = 20d/180
    private val MOVE_POWER = 0.4
    private val STATIC_POWER = 0.17
    private val ACCEL_TIME = 200L
    
    private var m_start = 0L
    
    
    override def initialize {
      val dir = position > Pitcher.position
      /**
       * Close Claw Shit
       */
      
      m_start = System.currentTimeMillis()
    }
    
    override def execute {
      val offset = position - Pitcher.position
      if (offset.abs < DECEL_RANGE){
        val power = ((MOVE_POWER - STATIC_POWER) * Math.abs(offset) / DECEL_RANGE) + STATIC_POWER
        Pitcher.power = if (offset > 0) -power else power 
      } else {
        val accelTime = System.currentTimeMillis() - m_start
			  if (accelTime < ACCEL_TIME) 
				  Pitcher.power = offset.signum * (-MOVE_POWER * (ACCEL_TIME - accelTime)) / ACCEL_TIME
		  	else
          Pitcher.power = offset.signum * -MOVE_POWER
      } 
    }
    
    override def end = GBScheduler <= HoldPitcher(state)
      
    
  }
  
  case class HoldPitcher(state : State.PitcherState) extends Command with PitcherCommand{
    def isFinished = false
    
    val STATIC_POWER = 0.17
    
    override def execute {
      state match {
        case State.Of.Relaxed() => {
          Pitcher.stop
        } 
        case _ => {
          val equalibriumOffset = Pitcher.angle - Pitcher.equalibriumState
    	    val power = (STATIC_POWER * math.sin(equalibriumOffset.toRadians)).abs
    		  Pitcher.power = equalibriumOffset.signum * power 
   
          GBScheduler <= Conditional(new MovePitcherToState(state), (position - Pitcher.position).abs > tolerance);
        } 
      }
    }
  }
  
  
  
}