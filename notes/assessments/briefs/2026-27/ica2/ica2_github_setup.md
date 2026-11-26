---
title: "ICA2 - Setting Up Git and GitHub in IntelliJ"
module-code: COMP C8Z03
module-title: Object-Oriented Programming
programme: "BSc (Hons) in Computing in Games Development; BSc (Hons) in Computing in Software Development"
stage: 2
institution: DkIT
author: OOP Teaching Team
academic-year: 2026/27
last-updated: 2026-11-26
doc-type: assessment-guide
related-brief: 2026-27-l8-s1-oop-ica2.md
---

# ICA2 - Setting Up Git and GitHub in IntelliJ

This guide shows you how to connect IntelliJ IDEA to GitHub, and how to set up your ICA2 project as a private GitHub repository. It goes with the [ICA2 brief](2026-27-l8-s1-oop-ica2.md), and covers steps 3 and 4 of Appendix B in more detail.

Follow the parts in order. The whole setup takes about 30 minutes. Do it in the first week of the project, not the night before show and tell 1.

> [!IMPORTANT]
> Your GitHub repository, your tags and your commit history are **must-haves** (brief, section 10). If any of them is missing, your project is not graded.

## In this guide

- [What you need before you start](#what-you-need-before-you-start)
- [Part 1 - Install Git](#part-1---install-git)
- [Part 2 - Tell Git who you are](#part-2---tell-git-who-you-are)
- [Part 3 - Connect IntelliJ to your GitHub account](#part-3---connect-intellij-to-your-github-account)
- [Part 4 - Create the ICA2 project](#part-4---create-the-ica2-project)
- [Part 5 - Put the project on GitHub](#part-5---put-the-project-on-github)
- [Part 6 - Add the lecturer and submit the link](#part-6---add-the-lecturer-and-submit-the-link)
- [Part 7 - Your everyday routine](#part-7---your-everyday-routine)
- [Part 8 - Push a show and tell tag](#part-8---push-a-show-and-tell-tag)
- [Part 9 - Check your repository from a fresh clone](#part-9---check-your-repository-from-a-fresh-clone)
- [Troubleshooting](#troubleshooting)
- [Setup checklist](#setup-checklist)

## What you need before you start

| You need | Details |
|:--|:--|
| A GitHub account | Sign up at [github.com](https://github.com/) if you do not have one. Your college email is fine. |
| IntelliJ IDEA | The version used in the labs. The free version is enough. |
| A JDK | JDK 25, the same as in the module notes. IntelliJ can download one for you in Part 4. |
| The starter code | The three interfaces in the [ICA2 starter folder](ica2_starter/), or the zip on Moodle |

Before you start, turn off the AI features in IntelliJ, as described in section 11 of the brief.

## Part 1 - Install Git

IntelliJ uses Git to track your changes. Git is a separate program, so it must be installed on your computer. Lab machines already have it.

1. Open IntelliJ. Go to **Settings > Version Control > Git**. On macOS, **Settings** is under the **IntelliJ IDEA** menu.
2. Click **Test** next to **Path to Git executable**.
3. If IntelliJ shows a Git version number, Git is installed. Go to Part 2.
4. If IntelliJ says Git is not found, click **Download and Install**. You can also install Git yourself from [the Git downloads page](https://git-scm.com/downloads), and accept the default options.
5. Restart IntelliJ, then click **Test** again to check it.

## Part 2 - Tell Git who you are

Git stamps every commit with a name and an email address. We read your commit history at the interview, so your real name must appear on every commit.

1. Open the **Terminal** tool window in IntelliJ (**View > Tool Windows > Terminal**).
2. Run these two commands. Use your own name, and the email address of your GitHub account.

```bash
git config --global user.name "Aoife Byrne"
git config --global user.email "aoife.byrne@example.com"
```

3. Run `git config --global --list` to check that both values are correct.

If you do not want your email address to be public, use the private `noreply` address that GitHub gives you. You find it at **GitHub > Settings > Emails**.

## Part 3 - Connect IntelliJ to your GitHub account

IntelliJ has GitHub support built in. You only need to log in once on each computer.

1. Go to **Settings > Version Control > GitHub**.
2. Click **+** (Add Account), then choose **Log In via GitHub**.
3. Your web browser opens a GitHub page. Log in if you need to, then click **Authorize**.
4. Go back to IntelliJ. Your GitHub username now appears in the list. Click **OK**.

If the browser login does not work, choose **Log In with Token** instead. Follow the link IntelliJ shows to create a token on GitHub, keep the scopes it selects for you, and paste the token into IntelliJ.

> [!CAUTION]
> A token works like a password. Never put a token in your code, your README or a commit. If you think someone has seen your token, delete it on GitHub at **Settings > Developer settings > Personal access tokens**.

On a shared lab computer, remove your account when you finish. Go to **Settings > Version Control > GitHub**, select your account, and click **-**.

## Part 4 - Create the ICA2 project

Use **Maven** as the build system. Maven stores the JUnit library in a file called `pom.xml`, which goes into your repository. This means your project compiles from a fresh clone on any computer, which is a hand-in requirement (brief, section 10).

### 4.1 Create a Maven project

1. Go to **File > New > Project**.
2. Choose **Java** on the left.
3. Fill in the form:

| Field | What to enter |
|:--|:--|
| **Name** | `L8-Y2-OOP-ICA2-STUDENT_NAME`, with your own name, for example `L8-Y2-OOP-ICA2-Aoife-Byrne` |
| **Location** | A folder on your own drive, not OneDrive, Google Drive or Dropbox |
| **Create Git repository** | Leave this unticked. Part 5 does it for you. |
| **Build system** | Maven |
| **JDK** | JDK 25. If you do not have it, choose **Download JDK**. |
| **Add sample code** | Untick it |
| **GroupId** (under **Advanced Settings**) | `ica2` |
| **ArtifactId** | Leave it as the project name |

4. Click **Create**.

The rules for the repository name are in section 10 of the brief. Use only the letters A to Z and hyphens, with no spaces, accents or apostrophes.

> [!WARNING]
> Do not keep your project in a OneDrive, Google Drive or Dropbox folder. These tools sync files while Git is writing them, and this can break your repository.

### 4.2 Add JUnit 5

1. Open `pom.xml` in the top folder of the project.
2. Add this block just before the closing `</project>` tag. If your `pom.xml` already has a `<dependencies>` section, add only the `<dependency>` part inside it.

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

3. Click the **Load Maven Changes** button that appears at the top right of the editor. You can also open the **Maven** tool window and click **Reload All Maven Projects**.

The [JUnit in IntelliJ cheat sheet](../../../../shared/cheat%20sheets/cheatsheet_junit_in_intellij.md) has more help if IntelliJ still cannot find JUnit.

### 4.3 Add the starter code

1. In the **Project** tool window, right-click `src/main/java` and choose **New > Package**. Name it `ica2.api`.
2. Copy the three starter files, `HelpDesk.java`, `DeskCommand.java` and `TicketFilter.java`, into the `ica2.api` package.
3. Create a second package called `ica2.model`, in `src/main/java`.
4. In `ica2.model`, create the empty classes `Ticket` and `Agent`, and the empty enums `Status` and `Priority`.
5. Go to **Build > Build Project**. The interfaces now compile.

Your tests go in `src/test/java`, in the same package names as the classes they test.

### 4.4 Add your README and progress log

1. Right-click the top folder of the project, and choose **New > File**. Name it `README.md`.
2. Do the same again, and name the second file `PROGRESS.md`.
3. Type a heading into each file for now, such as `# ICA2 - Help Desk`. Section 7 of the brief lists what goes in each file.

## Part 5 - Put the project on GitHub

IntelliJ can create the GitHub repository, make your first commit and upload it, all in one step.

1. Go to **Git > GitHub > Share Project on GitHub**. If you see a **VCS** menu instead of a **Git** menu, the same item is under **VCS > Share Project on GitHub**.
2. Fill in the dialog:

| Field | What to enter |
|:--|:--|
| **Repository name** | `L8-Y2-OOP-ICA2-STUDENT_NAME`, the same as your project name |
| **Private** | Tick it |
| **Remote** | Leave it as `origin` |
| **Description** | `ICA2 Help Desk - COMP C8Z03 OOP` |

3. Click **Share**.
4. IntelliJ asks which files to add to the first commit. Tick the `src` folder, `pom.xml`, `.gitignore`, `README.md` and `PROGRESS.md`. Leave `target` unticked.
5. Type a commit message that follows the [commit message guidelines](https://github.com/nmcguinness/L8-OOP-Module-Content/blob/main/notes/shared/general/commit-message-guidelines.md), for example `chore: add starter interfaces and empty model classes`.
6. Click **Add**.

IntelliJ shows a link to your new repository when it finishes. Open the link and check two things on the GitHub page:

- The word **Private** appears next to the repository name.
- Your files are there.

> [!CAUTION]
> Your repository must be **private**. A public repository lets other students copy your work, and you are both responsible if they do. If you made it public by mistake, go to the repository's **Settings > General**. At the bottom, under **Danger Zone**, click **Change visibility** and choose **Private**.

### What the .gitignore file does

The `.gitignore` file lists files that Git must not track. Maven creates one for you, which leaves out the `target` folder and IntelliJ's personal settings files. These files are made again on each computer, so they do not belong in your repository.

Do not add `reports` to `.gitignore`. Your coverage report in `reports/coverage/` must be committed (brief, section 6.3).

## Part 6 - Add the lecturer and submit the link

Do this straight after Part 5. It must be done before show and tell 1.

### 6.1 Add the lecturer as a collaborator

1. On your repository's GitHub page, click **Settings**.
2. Click **Collaborators** on the left. GitHub may ask for your password again.
3. Click **Add people**.
4. Type the lecturer's GitHub username, `nmcguinness`, and choose it from the list.
5. Click **Add to repository**.

The lecturer appears as **Pending** until they accept the invitation. This is normal, and you do not need to do anything more.

### 6.2 Put the link on Moodle

1. Copy the address of your repository from your browser. It looks like `https://github.com/your-username/L8-Y2-OOP-ICA2-Aoife-Byrne`.
2. Paste it into the ICA2 repository link submission on Moodle.

## Part 7 - Your everyday routine

Each time you finish a method or a group of tests, commit and push. The brief asks for at least 30 commits, made in at least 4 different weeks.

### 7.1 Commit

1. Press **Ctrl+K** (**Cmd+K** on macOS), or click **Commit** in the tool bar on the left.
2. Tick the files you changed. Click a file to see exactly what you changed in it.
3. Type a commit message that follows the [commit message guidelines](https://github.com/nmcguinness/L8-OOP-Module-Content/blob/main/notes/shared/general/commit-message-guidelines.md).
4. Click **Commit**.

> [!WARNING]
> The commit window in IntelliJ can show a button that writes the commit message for you with AI. Do not use it. It counts as an AI tool under section 11 of the brief. Write every commit message yourself.

### 7.2 Push

A commit is saved only on your computer. A **push** uploads your commits to GitHub.

1. Press **Ctrl+Shift+K** (**Cmd+Shift+K** on macOS), or go to **Git > Push**.
2. Check the list of commits, then click **Push**.

You can also click **Commit and Push** in the commit window, to do both in one step.

### 7.3 Start on a different computer

If you work on a lab computer and at home, push before you leave one computer, and pull before you start on the other.

- To get a copy on a new computer the first time, go to **File > New > Project from Version Control**, choose **GitHub**, pick your repository, and click **Clone**.
- After that, go to **Git > Pull** each time you sit down to work.

## Part 8 - Push a show and tell tag

A **tag** is a name that marks one commit, like a bookmark. We mark the commit that each tag points to.

| Tag | Push it before |
|:--|:--|
| `st1` | Your show and tell 1 lab (Semester 3, Week 11) |
| `st2` | Your show and tell 2 lab (Semester 4, Week 1) |
| `final` | The hand-in deadline (Monday of Semester 4, Week 2, at 21:00) |

### 8.1 Tag in IntelliJ

1. Commit and push all your work first.
2. Go to **Git > New Tag**.
3. Type the tag name, for example `st1`, and click **Create Tag**.
4. Go to **Git > Push**. Tick **Push tags** at the bottom of the dialog, and click **Push**.

### 8.2 Tag in the terminal

You can do the same thing in the **Terminal** tool window:

```bash
git tag st1
git push origin st1
```

### 8.3 Check the tag on GitHub

On your repository's GitHub page, click the **Tags** link next to the branch name. Your tag must appear in the list. If it does not, it was not pushed, and we cannot see it.

If you tag the wrong commit, ask the lecturer before the deadline. Do not delete and re-create a tag after the deadline.

## Part 9 - Check your repository from a fresh clone

Your project must compile, and `Demo` must run, from a fresh clone (brief, section 10). Check this before each show and tell and before you hand in.

1. Go to **File > New > Project from Version Control**.
2. Choose **GitHub**, pick your repository, and set **Directory** to a new, empty folder, for example on your desktop.
3. Click **Clone**, and open the project when IntelliJ asks.
4. Wait for Maven to finish loading, then run `Demo` and your tests.

If something is missing in the fresh clone, it was not committed or not pushed. Go back to your normal project, commit and push the missing file, then try again. Delete the test folder when you finish.

## Troubleshooting

| Problem | What to do |
|:--|:--|
| **Share Project on GitHub** is not in the menu | Check that the GitHub plugin is on, at **Settings > Plugins > Installed**. Then check Part 3. |
| Push fails with "rejected" or "fetch first" | GitHub has a commit that your computer does not, often because you edited a file on the GitHub website. Go to **Git > Pull**, then push again. |
| Push fails with "authentication failed" | Your login has expired. Remove and add your account again (Part 3). |
| Commits show the wrong name on GitHub | Check `git config --global --list` (Part 2). Commits made before the fix keep the old name, so tell the lecturer. |
| The `target` folder was committed | Make sure `target/` is in `.gitignore`. In the terminal, run `git rm -r --cached target`, then commit and push. |
| "Cannot resolve symbol" for JUnit | Reload Maven (Part 4.2). See the [JUnit in IntelliJ cheat sheet](../../../../shared/cheat%20sheets/cheatsheet_junit_in_intellij.md). |
| You created the repository on the GitHub website instead | That is fine. Clone it as in Part 7.3, then create the Maven project inside the cloned folder. |

If you are still stuck, bring your laptop to the lab, or ask on the module forum on Moodle. Write down the exact error message.

## Setup checklist

| Item | Done |
|:--|:--|
| Git installed, and **Test** in IntelliJ shows a version | [ ] |
| `user.name` and `user.email` set to your own details | [ ] |
| GitHub account added in IntelliJ | [ ] |
| Maven project named `L8-Y2-OOP-ICA2-STUDENT_NAME`, with JUnit 5 in `pom.xml` | [ ] |
| Starter interfaces in `ica2.api`, empty model classes in `ica2.model`, project builds | [ ] |
| `README.md` and `PROGRESS.md` in the top folder | [ ] |
| Repository on GitHub, marked **Private** | [ ] |
| Lecturer added as a collaborator | [ ] |
| Repository link on Moodle | [ ] |
| A fresh clone builds and runs | [ ] |
