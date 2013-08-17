package daos

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._

import testhelpers.AppTesting

import models._

class UserDaoSpec extends Specification with AppTesting {

  "UserDao" should {
    "create an user" in {
      running(appWithInMemoryDb) {
        val user: User = UserDao.createUser(username = "manager")
        assert(user.username === "manager")
      }
    }

    "persist user when creating it" in {
      running(appWithInMemoryDb) {
        val user: User = UserDao.createUser(username = "manager")
        assert(user.isPersisted)
      }
    }

    "retrieve an user" in new WithDbData {
      def prepareDbWithData() {
        UserDao.createUser(username = "manager")
      }
      val user = UserDao.findByUsername("manager").get
      assert(user.username === "manager")
    }
    
    "retrieve by ID" in new WithDbData {
      var id:Long = _
      def prepareDbWithData() {
        val user = UserDao.createUser(username = "manager")
        id = user.id
      }
      val user = UserDao.findById(id).get
      assert(user.username === "manager")
    }

    "have persisted users when retrieving" in new WithDbData {
      def prepareDbWithData() {
        UserDao.createUser(username = "manager")
      }
      val user = UserDao.findByUsername("manager").get
      assert(user.isPersisted)
    }
    
    "should find all users" in new WithDbData {
      def prepareDbWithData() {
        UserDao.createUser(username = "manager")
        UserDao.createUser(username = "secretary")
        UserDao.createUser(username = "employee")
      }
      val users = UserDao.findAll()
      assert(users.map(_.username).toSet === Set("manager", "secretary", "employee"))
    }

  }

}