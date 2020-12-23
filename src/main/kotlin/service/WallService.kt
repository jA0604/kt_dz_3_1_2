package service

import PostNotFoundException
import model.Comment
import model.Likes
import model.Post
import java.lang.Exception

class WallService {

    private var id = 0

    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()

    fun add(post: Post): Post {
        val copy = post.copy(id = id++)
        posts += copy
        return copy
    }

    fun remove(removeId: Int): Int {
        val postWithoutId = posts.filterIndexed { index, s -> (index != removeId) }
        posts = postWithoutId.toTypedArray()
        return --id
    }

    fun findById(id: Int): Post {
        for (post in posts) {
            if (post.id == id) {
                return post
            }
        }
        return throw PostNotFoundException("no post with id $id")
    }

    fun likeById(id: Int): Int {
        posts.forEachIndexed { index, post ->
            if (index == id) {
                val countLikes = posts[index].likes.countLikes + 1
                val countDislakes = posts[index].likes.countDislikes
                posts[index] = posts[index].copy(likes = Likes(countLikes, countDislakes))
                return posts[index].likes.countLikes
            }
        }
        return 0
    }

    fun dislikeById(id: Int): Int {
        for (post in posts) {
            if (post.id == id) {
                val countLikes = posts[id].likes.countLikes
                val countDislakes = posts[id].likes.countDislikes + 1
                posts[id] = posts[id].copy(likes = Likes(countLikes, countDislakes))
                return posts[id].likes.countDislikes
            }
        }
        return 0
    }

    fun sizeWallPosts() = posts.size

    fun sizeWallComments() = comments.size



    fun update(post: Post): Boolean {
        posts.map {
            if (it.id == post.id) {
                val itId = it.id
                val itOwnerId = it.ownerId
                val itDate = it.date
                val copy = post.copy(id = itId, ownerId = itOwnerId, date = itDate)
                posts.set(itId, copy)
                return true
            }
        }
        return false
    }

    fun get(id: Int) = posts[id]


    fun createComment(comment: Comment, idPost: Int) {
        for (post in posts) {
            if (post.id == idPost) {
                val copyComment = comment.copy(id = comment.id + 1)
                comments += copyComment
                return
            }
        }
        return throw PostNotFoundException("No post with id $idPost")

    }

}

