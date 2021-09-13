var users = db.getCollection('user').find({});

users.forEach(function(user) {
    user.email = user.email.toLowerCase();
    if (user.loginInfo) {
        user.loginInfo.providerKey = user.loginInfo.providerKey.toLowerCase();
    }
    db.user.save(user);
});

var passwordInfos = db.getCollection('auth.PasswordInfo').find({});

var oldEmails = [];
passwordInfos.forEach(function (passwordInfo) {
    var oldEmail = passwordInfo['_id'].providerKey;
    var needUpdate = passwordInfo['_id'].providerKey !== passwordInfo['_id'].providerKey.toLowerCase();
    
    if (needUpdate) {
        passwordInfo['_id'].providerKey = passwordInfo['_id'].providerKey.toLowerCase();
        db.getCollection('auth.PasswordInfo').save(passwordInfo);
        db.getCollection('auth.PasswordInfo').remove({'_id.providerKey': oldEmail})
    }
})