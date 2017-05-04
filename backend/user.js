var mongoose = require("mongoose");

var userSchema = mongoose.Schema({
  username: String,
  password: String,
  facebookId: String
});

mongoose.model("User", userSchema);