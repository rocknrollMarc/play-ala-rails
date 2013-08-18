package daos

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import testhelpers.AppTesting

import models._


class PostDaoSpec extends Specification with AppTesting {

  trait WithTestData extends WithDbData {
    var user: User = _
    var post: Post = _
    def prepareDbWithData() {
      user = UserDao.createUser(username = "manager")
      post = PostDao.createPost(user, text = "Hello there")
    }
  }

  "UserDao" should {

    "create a post" in {
      running(appWithInMemoryDb) {
        val user = UserDao.createUser("manager")
        val post: Post = PostDao.createPost(user, text = "Hello there")
        assert(post.isPersisted)
        assert(post.text === "Hello there")
        assert(post.user.toOption.get === user)
      }
    }

    "retrieve posts" in new WithTestData {
      val posts = PostDao.findAllForUser(user)
      assert(posts.head.text === "Hello there")
    }

  }

}