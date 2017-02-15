# Notes on CS652 dev process

See [Why Program by Hand in Five Days what You Can Spend Five Years of Your Life Automating?](https://www.infoq.com/presentations/Automation-DSL)

Automation is not just about productivity. It's about:

* consistency, which is critical for testing. If things sometimes work and sometimes don't, you don't know if the software change is causing a problem.
* correctness: automation can make critical things work only one way, the right way (murphy's law) rocket sled and IBM keyboards:
<br>
<img src=http://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/Ibm_pc_5150.jpg/500px-Ibm_pc_5150.jpg width=400><br>
<img src=http://images.pcworld.com/images/article/2011/08/rough_pc_07-5206179.jpg width=500>
* (Remember: testing doesn't improve quality; that just measures it and tells you how screwed you are.)
* Quality must be baked in and automation is one way to do that.

Automation: checkout stand where they manually enter prices. The solution was not to train people better or to give them negative reinforcement or positive reinforcement. Better to make it impossible to make a mistake with a laser.

<img src=http://public.media.smithsonianmag.com//filer/3c/4d/3c4d59ba-0401-428e-a8ad-75ad8cd292b3/marsh-supermarket-barcode.jpg>

## Github

git is a tool. github is a website. some of you are confusing that.

The first step in working on a project is to create a repository. In the case of our course, you will create a repository at github per a link I sent around. Then you will begin work in that repository. Working outside of the repository is like working on a private machine instead of your company machine.

It would be like developing aircraft control software and the day before the airplane is to launch, you test it on the airplane. You should be continuously checking your software on the real aircraft, or at least a damn good simulator.
 
github is also a free backup. (more later)

github is not like canvas; it is not a place where you submit your files. it is an active part of your development process.

See [git basics](https://github.com/parrt/cs601/blob/master/lectures/git-basics.md) and [git collaboration process](https://github.com/parrt/cs601/blob/master/lectures/github-dev.md).

Show process for multiple programmer interaction, but using single master branch. clone vs pull/push. show non-central repo mechanism.

## Maven

I give you a pom.xml file that specifically says how to build your project and it is what is used over at Travis.

Show how to use in intellij.

## Travis

jGuru and a variety of different boxes, slightly different versions of the operating system.

one of the most powerful debugging tools is: **what is the difference between what works and does not work.**

when it doesn't work at travis, ask yourself what the difference between the systems is? Are you using maven on your local box? If not, try that first. 

If there is a different still then gosh it must be that you have hardcoded a path or done something that relies specifically on the configuration of your computer.

## General notes

draw dev, test, live box. test is like Travis.

Some stuff taken from [Little bits of development wisdom](http://parrt.cs.usfca.edu/doc/devnybbles.html):

* All machines of a certain class (web or db etc...) must be identical down to the exact version of Linux. Reproducibility is important. You must be certain that your test and live environments are identical if you want a chance of finding bugs.
* To go from raw linux box in a known state to fully configured system ready to bring live must be completely automated. You should be able to install a few RPMs or tar balls, push your software, and go live. Reproducibility!
* Avoid system components that force GUI or webpage initialization / configuration. Automation is the goal and a GUI configuration tool destroys any hope of configuring a box by unzipping or installing RPMs.
* Machines where you deploy or test software must be READONLY. You are not tempted to tweak the config or software on the live system (even if you use a repository to deploy).
* Verify that your backup strategy works (i.e., you can bring back data) and that it continues to operate. Back up onto hard drives if you can and then onto tape (shudder) or DVD-RAMs. See [gitlabs debacle](https://techcrunch.com/2017/02/01/gitlab-suffers-major-backup-failure-after-data-deletion-incident/).
