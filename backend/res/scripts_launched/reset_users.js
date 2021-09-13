var users = db.getCollection('user').find({});

users.forEach(function (user) {
    delete user.config.currentStep;
    delete user.config.domain_ids;
    delete user.config.introspection_stream_ids;
    delete user.config.stream_ids;
    delete user.config.criterion_ids;
    delete user.config.detail_ids;
    delete user.config.sawFinalPopup;
    db.user.save(user);
})