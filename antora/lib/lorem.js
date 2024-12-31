// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements. See the NOTICE file distributed with this
// work for additional information regarding copyright ownership. The ASF
// licenses this file to you under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with the
// License. You may obtain a copy of the License at.
//
// http://www.apache.org/licenses/LICENSE-2.0 .
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR  CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations
// under the License.


// as per https://asciidoctor-docs.netlify.com/asciidoctor.js/extend/extensions/block-macro-processor/
// NOTE: Below we are using a minimalist implementation to generate lorem ipsum text.
// If you need a complete implementation, you can use the following Node package:
// var lorem = require('lorem-ipsum')

var dictionary = {
  words: [
    'lorem',
    'ipsum',
    'dolor',
    'sit',
    'amet',
  ],
}

function getRandomArbitrary (min, max) {
  return Math.random() * (max - min) + min
}

function lorem (opts) {
  var count = opts.count
  var units = opts.units
  var words = dictionary.words
  if (units === 'sentences') {
    var sentences = []
    var sentence = []
    for (var i = 0; i < count; i++) {
      var sentenceLength = getRandomArbitrary(5, 15)
      for (var j = 0; j < sentenceLength; j++) {
        // use predictive position for testing purpose
        var position = j % words.length
        // var position = Math.floor(Math.random() * words.length);
        var word = dictionary.words[position]
        if (j === 0) {
          // capitalize the first letter
          word = word.charAt(0).toUpperCase() + word.slice(1)
        }
        sentence.push(word)
      }
      sentence[sentence.length - 1] += '.'
      sentences.push(sentence.join(' '))
    }
    return sentences.join(' ')
  }
}

module.exports = function (registry) {
  registry.blockMacro(function () {
    var self = this
    self.named('lorem')
    self.process(function (parent, target, attrs) {
      var num = parseInt(attrs.num) || 3
      //var result = 'yada yada yada'//lorem({ count: size, units: target })
      var result = "_" + lorem({ count: num, units: target }) + "_"
      return self.createBlock(parent, 'paragraph', result)
    })
  })
}
