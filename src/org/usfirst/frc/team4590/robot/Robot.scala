package org.usfirst.frc.team4590.robot

import edu.wpi.first.wpilibj.IterativeRobot

class Robot extends IterativeRobot{
  override def robotInit() = {}
  override def disabledInit() = {}
	override def disabledPeriodic() = {}
	override def autonomousInit() = {}
	override def autonomousPeriodic() = {}
	override def teleopInit() = {}
	override def teleopPeriodic() = {
	  GBScheduler.run()
	}
	override def testPeriodic() = {}
}