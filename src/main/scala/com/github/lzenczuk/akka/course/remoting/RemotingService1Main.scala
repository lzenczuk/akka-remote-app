package com.github.lzenczuk.akka.course.remoting

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
/**
  * Created by dev on 26/08/16.
  */

object CounterActor {
  sealed trait CounterMessage
  case object GetAndIncreaseByOne extends CounterMessage
  case class GetAndIncrease(v:Int) extends CounterMessage
  case class CounterValue(v:Int) extends CounterMessage
}

class CounterActor extends Actor with ActorLogging{
  import CounterActor._

  var counter = 0

  def getAndIncrease(v:Int):Int = {
    val tmpCounter = counter
    counter+=v

    log.info("Counter increased")

    tmpCounter
  }


  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info("---------------> Starting counter actor")
  }

  override def receive = {
    case GetAndIncreaseByOne => sender() ! CounterValue(getAndIncrease(1))
    case GetAndIncrease(v) => sender() ! CounterValue(getAndIncrease(v))
  }
}

object RemotingService1Main extends App{

  private val config: Config = ConfigFactory.load().getConfig("service1-config")

  // Actor system name from configuration
  private val system: ActorSystem = ActorSystem("Service1", config)

  //private val counterActor: ActorRef = system.actorOf(Props[CounterActor],"counter-0")
}
