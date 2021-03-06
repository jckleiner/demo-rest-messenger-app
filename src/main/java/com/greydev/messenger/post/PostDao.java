package com.greydev.messenger.post;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.greydev.messenger.SessionFactorySingleton;
import com.greydev.messenger.post.comment.Comment;
import com.greydev.messenger.post.comment.CommentDao;
import com.greydev.messenger.profile.Profile;

public class PostDao {

	private static SessionFactory factory = SessionFactorySingleton.getSessionFactory();

	public static Post getPost(Long id) {
		Post result = null;
		final Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			result = session.get(Post.class, id);

			transaction.commit();
			session.close();

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return result;

	}

	public static Long addPostToProfile(String parentProfileName, Post post) {
		Long savedEntityId = null;
		final Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			Profile parentProfile = session.get(Profile.class, parentProfileName);
			parentProfile.getPosts().add(post);
			post.setProfile(parentProfile);
			// this also saves the Comment collection inside the post object
			savedEntityId = (Long) session.save(post);

			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return savedEntityId;
	}

	public static Post updatePost(Post post) {
		final Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			// first delete comments from the post object which will be updated
			List<Comment> commentsToDelete = CommentDao.getCommentsForPost(post.getId());
			commentsToDelete.forEach(comment -> {
				System.out.println("Deleting comment ... " + comment.getAuthor());
				session.delete(comment);
			});
			// update/replace old post object with the new one
			session.update(post);

			transaction.commit();
			session.close();

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			return null;
		}
		return post;

	}

	public static Post deletePost(Long id) {
		Post postToDelete = null;
		final Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			// TODO how to handle this properly?
			postToDelete = getPost(id);
			if (postToDelete != null) {
				session.delete(postToDelete);
			}

			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return postToDelete;
	}

	public static List<Post> getAllPostsAsList() {
		List<Post> results = null;
		final Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			results = session.createQuery("from Post").list();

			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return results;
	}

	public static List<Post> getPostsForProfile(String profileName) {
		List<Post> results = null;
		final Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			results = session.createQuery("from Post where profile.profileName='" + profileName + "'").list();

			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return results;
	}

	public static List<Post> getAllPostsForYear(int year) {
		List<Post> resultSet = new ArrayList<>();

		//		for (Post post : getAllPostsAsList()) {
		//			if (post.getCreated().get(Calendar.YEAR) == year) {
		//				resultSet.add(post);
		//			}
		//		}
		return resultSet;
	}

}
