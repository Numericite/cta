function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
};

var selections = db.getCollection('selection').find({});

selections.forEach(function (selection) {
  var tmpVersion = db.getCollection('version').find({"kind":"activity", "parent_id": selection.activity_id, "isDefault": true});
  
  print(tmpVersion.length())
  
  if (tmpVersion.length() === 0) {
      const newVersion = {
        id: guid(),
        name: 'Version 1',
        isDefault: true,
        kind: 'activity',
        parent_id: selection.activity_id,
        created_date: new Date().getTime()
      };
      
      db.getCollection('version').save(newVersion)
      
      selection.version_id = newVersion.id
      
      db.selection.save(selection)
  } else {
      selection.version_id = tmpVersion[0].id
      db.selection.save(selection)
  }
});