var versions = db.getCollection('version').find({});
versions.forEach(function (version) {
  var parent_id = version.file_id;
  delete version.file_id;
  version.parent_id = parent_id;
  version.kind = 'file'
  db.getCollection("version").save(version);
})
