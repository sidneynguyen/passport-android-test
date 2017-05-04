var express = require("express");
var path = require("path");
var bodyParser = require("body-parser");
var mongoose = require("mongoose");
var session = require("express-session");
var passport = require("passport");
var LocalStrategy = require("passport-local").Strategy;
var FacebookTokenStrategy = require('passport-facebook-token');
require("./user.js")
var User = mongoose.model("User");
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/passport-test');

var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(session({
  secret: 'secret',
  saveUninitialized: true,
  resave: true
}));
app.use(passport.initialize());
app.use(passport.session());

app.get("/me", function(req, res) {
    res.json({
        isAuthenticated: req.isAuthenticated()
    });
});

app.post("/register", function(req, res) {
  var user = new User({
    username: req.body.username,
    password: req.body.password
  });
  user.save(function(err, user) {
    if (err) {
      return res.send(err);
    }
    res.json(user);
  });
});

app.post("/login", passport.authenticate("local"), function(req, res) {
  res.json(req.user);
});

app.get('/logout', function(req, res) {
  req.logout();
  res.json({
    isAuthenticated: req.isAuthenticated()
  });
});

app.post('/facebook/token',
  passport.authenticate('facebook-token'),
  function (req, res) {
    res.json({
      isAuthenticated: req.isAuthenticated()
    });
  }
);

passport.use(new LocalStrategy(function(username, password, done) {
  User.findOne({username: username}, function(err, user) {
    if (err) {
      return resizeBy.send(err);
    }
    if (user.password != password) {
      return done(null, false, {msg: "Incorrect username or password"});
    }
    return done(null, user);
  });
}));

passport.use(new FacebookTokenStrategy({
    clientID: "1145949772201165",
    clientSecret: "371285fd4204ee8c9e02b6e96adf45e3"
  }, function(accessToken, refreshToken, profile, done) {
    User.findOne({facebookId: profile.id}, function(err, user) {
      if (!user) {
        var newUser = new User({
          facebookId: profile.id
        });
        newUser.save(function(err, user) {
          return done(err, user);
        });
      } else {
        return done(err, user);
      }
    });
}));

passport.serializeUser(function(user, done) {
  done(null, user._id);
});

passport.deserializeUser(function(id, done) {
  User.findById(id, function(err, user) {
    done(err, user);
  });
});

var port = 3000;
app.listen(port, function() {
  console.log("Server started on port " + port);
});