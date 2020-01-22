var sidebar = $('#sidebar').w2sidebar({
	name: 'sidebar',
	icon: 'icon-folder',
	nodes: 
		
		{ id: 'home', text: 'home', img: 'icon-folder', expanded: true, group: true,
		  nodes: [ { id: 'general', text: 'general', img: 'icon-folder', 
					 nodes: [
					   { id: 'program', text: 'programs', img: 'icon-folder',
						 nodes: [
									 { id: 'cpp', text: 'cpp', img: 'icon-folder',
										nodes: [
											 { id: 'cpp1', text: 'abc.cpp', img: 'icon-page' },
											 { id: 'cpp2', text: 'xyz.cpp', img: 'icon-page' }
										]
									},
									{ id: 'java', text: 'java', img: 'icon-folder',
										nodes: [
											 { id: 'java3', text: 'abc.java', img: 'icon-page' },
											 { id: 'java4', text: 'xyz.java', img: 'icon-page' }
										]
									}
								]
					   },
					   { id: 'lums', text: 'lums', img: 'icon-folder',
						 nodes: [
									 { id: 'standard', text: 'standard', img: 'icon-folder',
										nodes: [
											 { id: 'std1', text: 'abc.sas', img: 'icon-page' },
											 { id: 'std2', text: 'xyz.sas', img: 'icon-page' }
										]
									},
									{ id: 'specific', text: 'specific', img: 'icon-folder',
										nodes: [
											 { id: 'std3', text: 'dse.sas', img: 'icon-page' },
											 { id: 'std4', text: 'ert.sas', img: 'icon-page' }
										]
									}
								]
					   }
					  
					   ]
					},
					{ id: 'dev', text: 'dev', img: 'icon-folder', 
					 nodes: [
					   { id: 'cmp1', text: 'Compound1', img: 'icon-folder',
						 nodes: [
									 { id: 'study1', text: 'Study1', img: 'icon-folder',
										nodes: [
											 { id: 'object1', text: 'object1', img: 'icon-page' },
											 { id: 'object2', text: 'object2', img: 'icon-page' }
										]
									},
									{ id: 'study2', text: 'Study2', img: 'icon-folder',
										nodes: [
											 { id: 'object3', text: 'object1', img: 'icon-page' },
											 { id: 'object4', text: 'object2', img: 'icon-page' }
										]
									}
								]
					   },
					   { id: 'cmp2', text: 'Compound2', img: 'icon-folder',
						 nodes: [
									 { id: 'study3', text: 'Study1', img: 'icon-folder',
										nodes: [
											 { id: 'object5', text: 'object1', img: 'icon-page' },
											 { id: 'object6', text: 'object2', img: 'icon-page' }
										]
									},
									{ id: 'study4', text: 'Study2', img: 'icon-folder',
										nodes: [
											 { id: 'object7', text: 'object1', img: 'icon-page' },
											 { id: 'object8', text: 'object2', img: 'icon-page' }
										]
									}
								]
					   }
					  
					   ]
					},
					{ id: 'qa', text: 'qa', img: 'icon-folder', 
					nodes: [
					   { id: 'cmp3', text: 'Compound1', img: 'icon-folder',
						 nodes: [
									 { id: 'study5', text: 'Study1', img: 'icon-folder',
										nodes: [
											 { id: 'object9', text: 'object1', img: 'icon-page' },
											 { id: 'object10', text: 'object2', img: 'icon-page' }
										]
									},
									{ id: 'study6', text: 'Study2', img: 'icon-folder',
										nodes: [
											 { id: 'object11', text: 'object1', img: 'icon-page' },
											 { id: 'object12', text: 'object2', img: 'icon-page' }
										]
									}
								]
					   },
					   { id: 'cmp4', text: 'Compound2', img: 'icon-folder',
						 nodes: [
									 { id: 'study7', text: 'Study1', img: 'icon-folder',
										nodes: [
											 { id: 'object13', text: 'object1', img: 'icon-page' },
											 { id: 'object14', text: 'object2', img: 'icon-page' }
										]
									},
									{ id: 'study8', text: 'Study2', img: 'icon-folder',
										nodes: [
											 { id: 'object15', text: 'object1', img: 'icon-page' },
											 { id: 'object16', text: 'object2', img: 'icon-page' }
										]
									}
								]
					   }
					  
					   ]
					},
					{ id: 'prd', text: 'prd', img: 'icon-folder', 
					 nodes: [
					   { id: 'cmp5', text: 'Compound1', img: 'icon-folder',
						 nodes: [
									 { id: 'study9', text: 'Study1', img: 'icon-folder',
										nodes: [
											 { id: 'object17', text: 'object1', img: 'icon-page' },
											 { id: 'object18', text: 'object2', img: 'icon-page' }
										]
									},
									{ id: 'study10', text: 'Study2', img: 'icon-folder',
										nodes: [
											 { id: 'object19', text: 'object1', img: 'icon-page' },
											 { id: 'object20', text: 'object2', img: 'icon-page' }
										]
									}
								]
					   },
					   { id: 'cmp6', text: 'Compound2', img: 'icon-folder',
						 nodes: [
									 { id: 'study11', text: 'Study1', img: 'icon-folder',
										nodes: [
											 { id: 'object21', text: 'object1', img: 'icon-page' },
											 { id: 'object22', text: 'object2', img: 'icon-page' }
										]
									},
									{ id: 'study12', text: 'Study2', img: 'icon-folder',
										nodes: [
											 { id: 'object23', text: 'object1', img: 'icon-page' },
											 { id: 'object24', text: 'object2', img: 'icon-page' }
										]
									}
								]
					   }
					  
					   ]
					},
					{ id: 'users', text: 'users', img: 'icon-folder', 
					 nodes: [
					   { id: 'Pratt', text: 'pratt', img: 'icon-folder',
						 nodes: [
									 { id: 'prattpractice', text: 'practice-prgms', img: 'icon-folder',
										nodes: [
											 { id: 'oject17', text: 'object1', img: 'icon-page' },
											 { id: 'oject18', text: 'object2', img: 'icon-page' }
										]
									},
									{ id: 'submission', text: 'submissions', img: 'icon-folder',
										nodes: [
											 { id: 'object0000', text: 'final.asp', img: 'icon-page' },
											 { id: 'object2000', text: 'start.cs', img: 'icon-page' }
										]
									}
								]
					   },
					   { id: 'Jessica', text: 'jessica', img: 'icon-folder',
						 nodes: [
									 { id: 'mypractice', text: 'practice-programs', img: 'icon-folder',
										nodes: [
											 { id: 'obt21', text: 'abc.kt', img: 'icon-page' },
											 { id: 'obct22', text: 'xyz.py', img: 'icon-page' }
										]
									},
									{ id: 'myassignments', text: 'assignments', img: 'icon-folder',
										nodes: [
											 { id: 'bject23', text: 'project.bas', img: 'icon-page' },
											 { id: 'bject24', text: 'module.bas', img: 'icon-page' }
										]
									}
								]
					   }
					  
					   ]
					},
				   { id: 'file1', text: 'file1', img: 'icon-page' },
				   { id: 'file2', text: 'file2', img: 'icon-page' }
				 ]
		},
		onClick: function (event) {
			w2ui['myLayout'].content('top', 'id: ' + event.target);
		}
	});  