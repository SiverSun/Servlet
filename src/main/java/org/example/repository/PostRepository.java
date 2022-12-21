package org.example.repository;

import org.example.exception.NotFoundException;
import org.example.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    public List<Post> all() {
        if (posts.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = id.incrementAndGet();
            post = new Post(newId, post.getContent());
            posts.put(post.getId(), post);
        }
        if (posts.containsKey(post.getId())) {
            posts.put(post.getId(), post);
        } else {
            throw new NotFoundException();
        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}