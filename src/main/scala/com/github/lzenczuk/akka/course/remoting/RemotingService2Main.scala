package com.github.lzenczuk.akka.course.remoting

import akka.actor.{ActorRef, ActorSelection, ActorSystem, Address, AddressFromURIString, Deploy, Props}
import akka.pattern.ask
import akka.remote.RemoteScope
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dev on 26/08/16.
  */
object RemotingService2Main extends App{

  // Actor system name from configuration
  private val system: ActorSystem = ActorSystem("Service2", ConfigFactory.load().getConfig("service2-config"))

  // Getting location of remote actor
  //private val counterActorSelection: ActorSelection = system.actorSelection("akka.tcp://Service1@127.0.0.1:2552/user/counter-0")

  //private val remoteAddress: Address = AddressFromURIString("akka.tcp://Service1@127.0.0.1:2552/user/counter-0")
  //private val remoteCounterActor: ActorRef = system.actorOf(Props[CounterActor].withDeploy(Deploy(scope = RemoteScope(remoteAddress))))

  private val remoteCounterActor: ActorRef = system.actorOf(Props[CounterActor],"remote-counter")

  println(s"Remote actor path: ${remoteCounterActor.path}")

  implicit val timeout: Timeout = Timeout(1 second)

  (remoteCounterActor ? CounterActor.GetAndIncrease(5)).andThen{
    case Success(CounterActor.CounterValue(v)) => println(s"Counter: $v")
    case Failure(_) => println(s"Error")
  }

  (remoteCounterActor ? CounterActor.GetAndIncrease(7)).andThen{
    case Success(CounterActor.CounterValue(v)) => println(s"Counter: $v")
    case Failure(_) => println(s"Error")
  }
}