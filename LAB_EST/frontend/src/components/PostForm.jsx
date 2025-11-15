import React, { useState } from "react";
import axios from "axios";

export default function PostForm() {
  const [title, setTitle] = useState("");
  const [body, setBody] = useState("");
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
  e.preventDefault();
  setSuccess(false);

  try {
    const response = await axios.post("http://localhost:8080/posts", {
      title,
      body,
    });

    if (response.status === 201) {
      console.log("201 - Post created successfully!");
    }

    setSuccess(true);
    setTitle("");
    setBody("");

  } catch (err) {
    console.error("Error:", err);
  }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-xl shadow">
      <h2 className="text-2xl font-bold mb-4">Create Post</h2>

      {success && (
        <p className="mb-4 text-green-600 font-medium">
          Post submitted successfully!
        </p>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">

        <div>
          <label className="block mb-1 font-medium">Title</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full border rounded p-2"
            placeholder="Enter title"
            required
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Body</label>
          <textarea
            value={body}
            onChange={(e) => setBody(e.target.value)}
            className="w-full border rounded p-2"
            placeholder="Enter body"
            required
          ></textarea>
        </div>

        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700"
        >
          Submit
        </button>
      </form>
    </div>
  );
}
