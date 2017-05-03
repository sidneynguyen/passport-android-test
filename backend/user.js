var mongoose = require("mongoose");

var userSchema = mongoose.Schema({
  username: {
    type: String,
    index: true
  },
  password: String
});

mongoose.model("User", userSchema);