package org.usfirst.frc.team4590.robot.subsystem

import edu.wpi.first.wpilibj.command.Subsystem
import scala.collection.mutable.MutableList
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.networktables.NetworkTable

object Subsystems {
  
  val subsystems = MutableList[GBSubsystem]()
  
  abstract class GBSubsystem(name : String) extends Subsystem(name){
    
    subsystems += this
    
    def init : Unit
    
    def update : Unit
    
    def SD = NetworkTable.getTable("SmartDashboard")
     
  }
  
  def update = subsystems.foreach { sub => sub.update}
  
  val Pitcher = PitcherSubsystem
}